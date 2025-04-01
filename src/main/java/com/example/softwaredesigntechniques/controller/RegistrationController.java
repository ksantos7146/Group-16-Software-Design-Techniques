package com.example.softwaredesigntechniques.controller;

import com.example.softwaredesigntechniques.dto.registration.RegistrationDto;
import com.example.softwaredesigntechniques.dto.registration.RegistrationRequest;
import com.example.softwaredesigntechniques.dto.user.UserDto;
import com.example.softwaredesigntechniques.endpoint.registration.RegistrationEndpoint;
import com.example.softwaredesigntechniques.endpoint.user.UserEndpoint;
import com.example.softwaredesigntechniques.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/api/registrations")
@RequiredArgsConstructor
@Slf4j
public class RegistrationController {

    private final RegistrationEndpoint registrationEndpoint;
    private final UserEndpoint userEndpoint;

    @PostMapping
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> registerForEvent(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody RegistrationRequest registrationRequest) {
        try {
            log.info("Registering user for event: {}", registrationRequest);
            
            if (registrationRequest.getEventId() == null) {
                log.warn("Event ID is null in registration request");
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Event ID is required",
                    "timestamp", LocalDateTime.now()
                ));
            }
            
            // Get current user
            UserDto currentUser = userEndpoint.findByUsername(userDetails.getUsername());
            log.info("Current user: {}", currentUser);
            
            // Set the user ID in the request
            registrationRequest.setUserId(currentUser.getId());
            
            // Validate the populated request manually
            if (registrationRequest.getUserId() == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "User ID is required",
                    "timestamp", LocalDateTime.now()
                ));
            }
            
            // Check if user is already registered
            if (registrationEndpoint.existsByUserIdAndEventId(currentUser.getId(), registrationRequest.getEventId())) {
                log.warn("User {} is already registered for event {}", currentUser.getId(), registrationRequest.getEventId());
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "You are already registered for this event",
                    "timestamp", LocalDateTime.now()
                ));
            }
            
            // Register the user
            RegistrationDto registrationDto = registrationEndpoint.add(registrationRequest);
            log.info("Registration successful: {}", registrationDto);
            return ResponseEntity.ok(registrationDto);
        } catch (NotFoundException e) {
            log.error("Event or user not found: {}", registrationRequest, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error registering for event: {}", registrationRequest, e);
            return ResponseEntity.badRequest().body(Map.of(
                "error", e.getMessage(),
                "type", e.getClass().getSimpleName(),
                "timestamp", LocalDateTime.now()
            ));
        }
    }

    @GetMapping("/user")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<RegistrationDto>> getCurrentUserRegistrations(
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            UserDto currentUser = userEndpoint.findByUsername(userDetails.getUsername());
            List<RegistrationDto> registrations = registrationEndpoint.findByUserId(currentUser.getId());
            return ResponseEntity.ok(registrations);
        } catch (NotFoundException e) {
            log.error("User not found", e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/event/{eventId}")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> getEventRegistrationStatus(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long eventId) {
        try {
            UserDto currentUser = userEndpoint.findByUsername(userDetails.getUsername());
            Optional<RegistrationDto> registration = registrationEndpoint.findByUserIdAndEventId(
                    currentUser.getId(), eventId);
            
            if (registration.isPresent()) {
                return ResponseEntity.ok(Map.of(
                    "registered", true,
                    "registration", registration.get()
                ));
            } else {
                return ResponseEntity.ok(Map.of(
                    "registered", false
                ));
            }
        } catch (NotFoundException e) {
            log.error("User not found", e);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> cancelRegistration(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        try {
            // Get current user
            UserDto currentUser = userEndpoint.findByUsername(userDetails.getUsername());
            
            // Get the registration
            RegistrationDto registration = registrationEndpoint.get(id);
            
            // Check if the registration belongs to the current user
            if (!registration.getUser().getId().equals(currentUser.getId())) {
                return ResponseEntity.status(403).build(); // Forbidden
            }
            
            registrationEndpoint.delete(id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            log.error("Registration not found: {}", id, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error canceling registration: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }
} 