package com.example.softwaredesigntechniques.controller;

import com.example.softwaredesigntechniques.endpoint.event.EventEndpoint;
import com.example.softwaredesigntechniques.dto.event.EventDto;
import com.example.softwaredesigntechniques.dto.event.EventRequest;
import com.example.softwaredesigntechniques.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/events")
@RequiredArgsConstructor
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
    public ResponseEntity<EventDto> createEvent(@Valid @RequestBody EventRequest eventRequest) {
        try {
            return ResponseEntity.ok(eventEndpoint.add(eventRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<EventDto> getEvent(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(eventEndpoint.get(id));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<EventDto>> getAllEvents() {
        try {
            return ResponseEntity.ok(eventEndpoint.getAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/api/{id}")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EventDto> updateEvent(@PathVariable Long id, @Valid @RequestBody EventRequest eventRequest) {
        try {
            return ResponseEntity.ok(eventEndpoint.update(id, eventRequest));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        try {
            eventEndpoint.delete(id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 