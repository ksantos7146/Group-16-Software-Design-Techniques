package com.example.softwaredesigntechniques.controller;

import com.example.softwaredesigntechniques.domain.category.Category;
import com.example.softwaredesigntechniques.domain.event.Event;
import com.example.softwaredesigntechniques.dto.registration.RegistrationDto;
import com.example.softwaredesigntechniques.dto.user.UserDto;
import com.example.softwaredesigntechniques.endpoint.event.EventEndpoint;
import com.example.softwaredesigntechniques.endpoint.registration.RegistrationEndpoint;
import com.example.softwaredesigntechniques.endpoint.user.UserEndpoint;
import com.example.softwaredesigntechniques.dto.event.EventDto;
import com.example.softwaredesigntechniques.dto.event.EventRequest;
import com.example.softwaredesigntechniques.exception.NotFoundException;
import com.example.softwaredesigntechniques.service.event.impl.DefaultEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final EventEndpoint eventEndpoint;
    private final RegistrationEndpoint registrationEndpoint;
    private final UserEndpoint userEndpoint;
    private final DefaultEventService eventService;

    @GetMapping
    public String eventsPage() {
        return "events/list";
    }

    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String createEventPage() {
        return "events/create";
    }

    @GetMapping("/{id}")
    public String eventDetailPage() {
        return "events/detail";
    }

    @PostMapping
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> createEvent(@Valid @RequestBody EventRequest eventRequest) {
        try {
            log.info("Creating event with request: {}", eventRequest);
            log.info("Image ID from request: {}", eventRequest.getImageId());
            
            EventDto eventDto = eventEndpoint.add(eventRequest);
            log.info("Event created successfully: {}", eventDto);
            log.info("Created event has image? {}", (eventDto.getImage() != null));
            if (eventDto.getImage() != null) {
                log.info("Event image details - ID: {}, Name: {}", 
                         eventDto.getImage().getId(), 
                         eventDto.getImage().getFileName());
            }
            
            return ResponseEntity.ok(eventDto);
        } catch (Exception e) {
            log.error("Error creating event: {}", eventRequest, e);
            // Return a more detailed error message
            return ResponseEntity.badRequest().body(Map.of(
                "error", e.getMessage(),
                "type", e.getClass().getSimpleName(),
                "timestamp", LocalDateTime.now()
            ));
        }
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<EventDto> getEvent(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(eventEndpoint.get(id));
        } catch (NotFoundException e) {
            log.error("Event not found: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<EventDto>> getAllEvents() {
        try {
            log.info("Getting all events");
            
            // Get data through endpoint layer
            List<EventDto> events = eventEndpoint.getAll();
            log.info("Through endpoint - found {} events", events.size());
            
            for (EventDto event : events) {
                log.info("Event: id={}, title={}, startTime={}, endTime={}", 
                         event.getId(), event.getTitle(), event.getStartTime(), event.getEndTime());
            }
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            log.error("Error getting all events", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/api/{id}")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EventDto> updateEvent(@PathVariable Long id, @Valid @RequestBody EventRequest eventRequest) {
        try {
            log.info("Updating event {}: {}", id, eventRequest);
            EventDto eventDto = eventEndpoint.update(id, eventRequest);
            log.info("Event updated: {}", eventDto);
            return ResponseEntity.ok(eventDto);
        } catch (NotFoundException e) {
            log.error("Event not found for update: {}", id, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error updating event {}: {}", id, eventRequest, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        try {
            log.info("Deleting event: {}", id);
            eventEndpoint.delete(id);
            log.info("Event deleted: {}", id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            log.error("Event not found for delete: {}", id, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error deleting event: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/api/debug")
    @ResponseBody
    public String debugEvents() {
        try {
            log.info("DEBUG: Checking event data through endpoint");
            StringBuilder result = new StringBuilder();
            
            // Use the endpoint directly instead of casting it
            List<EventDto> events = eventEndpoint.getAll();
            
            result.append("Found ").append(events.size()).append(" events from endpoint:\n\n");
            
            for (EventDto event : events) {
                result.append("ID: ").append(event.getId())
                      .append(", Title: ").append(event.getTitle())
                      .append(", StartTime: ").append(event.getStartTime())
                      .append(", EndTime: ").append(event.getEndTime())
                      .append("\n");
            }
            
            return result.toString();
        } catch (Exception e) {
            log.error("Error in debug endpoint", e);
            return "Error: " + e.getMessage();
        }
    }

    @GetMapping("/my-registrations")
    @PreAuthorize("isAuthenticated()")
    public String myRegistrationsPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            UserDto currentUser = userEndpoint.findByUsername(userDetails.getUsername());
            List<RegistrationDto> registrations = registrationEndpoint.findByUserId(currentUser.getId());
            
            // Extract event IDs to get event details
            List<Long> eventIds = registrations.stream()
                    .map(reg -> reg.getEvent().getId())
                    .collect(Collectors.toList());
            
            // Get events for these registrations
            List<EventDto> registeredEvents = new ArrayList<>();
            for (Long eventId : eventIds) {
                try {
                    registeredEvents.add(eventEndpoint.get(eventId));
                } catch (NotFoundException e) {
                    log.warn("Event not found for registration: {}", eventId);
                }
            }
            
            model.addAttribute("events", registeredEvents);
            model.addAttribute("pageTitle", "My Registrations");
            return "events/my-registrations";
        } catch (Exception e) {
            log.error("Error getting user registrations", e);
            model.addAttribute("error", "Could not retrieve your registrations");
            return "events/list";
        }
    }
    
    @GetMapping("/my-events")
    @PreAuthorize("isAuthenticated()")
    public String myEventsPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            UserDto currentUser = userEndpoint.findByUsername(userDetails.getUsername());
            
            // Since we don't have a direct method to find events by creator, 
            // we'll get all events and filter on the client side for now
            List<EventDto> allEvents = eventEndpoint.getAll();
            // In a real app, we would add a method to the EventEndpoint interface
            // to get events by creator, but for this demo we'll just show all events
            
            model.addAttribute("events", allEvents);
            model.addAttribute("pageTitle", "My Events");
            return "events/my-events";
        } catch (Exception e) {
            log.error("Error getting user events", e);
            model.addAttribute("error", "Could not retrieve your events");
            return "events/list";
        }
    }
    
    @GetMapping("/categories")
    public String categoriesPage(Model model) {
        try {
            // Get all available categories
            List<Category> categories = Arrays.asList(Category.values());
            model.addAttribute("categories", categories);
            return "events/categories";
        } catch (Exception e) {
            log.error("Error getting categories", e);
            model.addAttribute("error", "Could not retrieve categories");
            return "events/list";
        }
    }
    
    @GetMapping("/category/{category}")
    public String eventsByCategory(@PathVariable String category, Model model) {
        try {
            Category cat = Category.valueOf(category.toUpperCase());
            List<EventDto> events = eventEndpoint.findByCategoriesContaining(cat);
            
            model.addAttribute("events", events);
            model.addAttribute("category", cat);
            model.addAttribute("pageTitle", cat.toString() + " Events");
            return "events/list";
        } catch (IllegalArgumentException e) {
            log.error("Invalid category: {}", category, e);
            model.addAttribute("error", "Invalid category");
            return "events/categories";
        } catch (Exception e) {
            log.error("Error getting events by category: {}", category, e);
            model.addAttribute("error", "Could not retrieve events for this category");
            return "events/categories";
        }
    }
    
    @GetMapping("/upcoming")
    public String upcomingEventsPage(Model model) {
        try {
            LocalDateTime now = LocalDateTime.now();
            List<EventDto> events = eventEndpoint.findByStartTimeBetween(now, now.plusMonths(1));
            
            model.addAttribute("events", events);
            model.addAttribute("pageTitle", "Upcoming Events");
            return "events/list";
        } catch (Exception e) {
            log.error("Error getting upcoming events", e);
            model.addAttribute("error", "Could not retrieve upcoming events");
            return "events/list";
        }
    }
    
    @GetMapping("/popular")
    public String popularEventsPage(Model model) {
        try {
            // Get events with most registrations
            List<EventDto> events = eventEndpoint.getAll();
            
            // Since EventDto doesn't have a registrationCount field,
            // we'll simply display all events for now
            // In a real app, we would add this field to EventDto
            
            model.addAttribute("events", events);
            model.addAttribute("pageTitle", "Popular Events");
            return "events/list";
        } catch (Exception e) {
            log.error("Error getting popular events", e);
            model.addAttribute("error", "Could not retrieve popular events");
            return "events/list";
        }
    }
} 