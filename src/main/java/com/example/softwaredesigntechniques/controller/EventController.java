package com.example.softwaredesigntechniques.controller;

import com.example.softwaredesigntechniques.endpoint.event.EventEndpoint;
import com.example.softwaredesigntechniques.dto.event.EventDto;
import com.example.softwaredesigntechniques.dto.event.EventRequest;
import com.example.softwaredesigntechniques.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final EventEndpoint eventEndpoint;

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
            EventDto eventDto = eventEndpoint.add(eventRequest);
            log.info("Event created successfully: {}", eventDto);
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
            return ResponseEntity.ok(eventEndpoint.getAll());
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
} 