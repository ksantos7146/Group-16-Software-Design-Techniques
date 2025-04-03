package com.example.softwaredesigntechniques.controller.api;

import com.example.softwaredesigntechniques.domain.event.Event;
import com.example.softwaredesigntechniques.domain.category.Category;
import com.example.softwaredesigntechniques.service.event.EventService;
import com.example.softwaredesigntechniques.endpoint.event.EventEndpoint;
import com.example.softwaredesigntechniques.dto.event.EventDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/events/api")
public class EventApiController {

    private final EventService eventService;
    private final EventEndpoint eventEndpoint;

    @Autowired
    public EventApiController(EventService eventService, EventEndpoint eventEndpoint) {
        this.eventService = eventService;
        this.eventEndpoint = eventEndpoint;
    }

    @GetMapping("/featured")
    public ResponseEntity<List<EventDto>> getFeaturedEvents() {
        try {
            log.info("Fetching featured events");
            List<EventDto> allEvents = eventEndpoint.getAll();
            
            if (allEvents == null || allEvents.isEmpty()) {
                log.warn("No events found");
                return ResponseEntity.ok(List.of());
            }
            
            List<EventDto> featuredEvents = allEvents.subList(0, Math.min(6, allEvents.size()));
            log.info("Returning {} featured events", featuredEvents.size());
            return ResponseEntity.ok(featuredEvents);
        } catch (Exception e) {
            log.error("Error fetching featured events: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(List.of());
        }
    }
    
    @GetMapping("/filtered")
    public ResponseEntity<Page<EventDto>> getFilteredEvents(
            @RequestParam(required = false) String category,
            @RequestParam(required = false, defaultValue = "ALL") String dateRange,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        
        try {
            log.info("Fetching filtered events with category: {}, dateRange: {}, search: {}", 
                category, dateRange, search);
                
            List<EventDto> allEvents = eventEndpoint.getAll();
            
            if (allEvents == null) {
                log.warn("No events found");
                return ResponseEntity.ok(Page.empty());
            }
            
            List<EventDto> filteredEvents = allEvents.stream()
                    .filter(event -> {
                        if (category != null && !category.isEmpty()) {
                            try {
                                Category cat = Category.valueOf(category);
                                if (event.getCategories() == null || 
                                    !event.getCategories().contains(cat)) {
                                    return false;
                                }
                            } catch (IllegalArgumentException e) {
                                log.warn("Invalid category: {}", category);
                            }
                        }
                        
                        if (search != null && !search.isEmpty()) {
                            String searchLower = search.toLowerCase();
                            boolean matchesTitle = event.getTitle() != null && 
                                event.getTitle().toLowerCase().contains(searchLower);
                            boolean matchesDesc = event.getDescription() != null && 
                                event.getDescription().toLowerCase().contains(searchLower);
                            
                            if (!matchesTitle && !matchesDesc) {
                                return false;
                            }
                        }
                        
                        if (!"ALL".equals(dateRange)) {
                            LocalDateTime now = LocalDateTime.now();
                            LocalDate today = LocalDate.now();
                            
                            switch (dateRange) {
                                case "TODAY":
                                    return event.getStartTime().toLocalDate().equals(today);
                                case "TOMORROW":
                                    return event.getStartTime().toLocalDate().equals(today.plusDays(1));
                                case "THIS_WEEK":
                                    LocalDate endOfWeek = today.plusDays(7 - today.getDayOfWeek().getValue());
                                    return !event.getStartTime().toLocalDate().isBefore(today) && 
                                           !event.getStartTime().toLocalDate().isAfter(endOfWeek);
                                case "NEXT_WEEK":
                                    LocalDate startOfNextWeek = today.plusDays(8 - today.getDayOfWeek().getValue());
                                    LocalDate endOfNextWeek = startOfNextWeek.plusDays(6);
                                    return !event.getStartTime().toLocalDate().isBefore(startOfNextWeek) && 
                                           !event.getStartTime().toLocalDate().isAfter(endOfNextWeek);
                                case "THIS_MONTH":
                                    return event.getStartTime().getMonth() == now.getMonth() && 
                                           event.getStartTime().getYear() == now.getYear();
                                default:
                                    return true;
                            }
                        }
                        
                        return true;
                    })
                    .collect(Collectors.toList());
            
            int start = page * size;
            int end = Math.min(start + size, filteredEvents.size());
            
            if (start >= filteredEvents.size()) {
                start = 0;
                end = Math.min(size, filteredEvents.size());
                page = 0;
            }
            
            List<EventDto> pageContent = filteredEvents.subList(start, end);
            Page<EventDto> pageResult = new PageImpl<>(pageContent, PageRequest.of(page, size), filteredEvents.size());
            
            log.info("Returning {} filtered events", pageContent.size());
            return ResponseEntity.ok(pageResult);
        } catch (Exception e) {
            log.error("Error fetching filtered events: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Page.empty());
        }
    }
} 