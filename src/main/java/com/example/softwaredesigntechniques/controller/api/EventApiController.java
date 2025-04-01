package com.example.softwaredesigntechniques.controller.api;

import com.example.softwaredesigntechniques.domain.event.Event;
import com.example.softwaredesigntechniques.service.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/events/api")
public class EventApiController {

    private final EventService eventService;

    @Autowired
    public EventApiController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/featured")
    public ResponseEntity<List<Event>> getFeaturedEvents() {
        try {
            // For now, just return the first few events as featured
            List<Event> allEvents = eventService.findAll();
            List<Event> featuredEvents = allEvents.subList(0, Math.min(6, allEvents.size()));
            return ResponseEntity.ok(featuredEvents);
        } catch (Exception e) {
            // Log the error
            System.err.println("Error fetching featured events: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(List.of()); // Return empty list on error
        }
    }
} 