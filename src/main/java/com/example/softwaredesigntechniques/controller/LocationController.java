package com.example.softwaredesigntechniques.controller;

import com.example.softwaredesigntechniques.dto.location.LocationDto;
import com.example.softwaredesigntechniques.dto.location.LocationRequest;
import com.example.softwaredesigntechniques.endpoint.location.LocationEndpoint;
import com.example.softwaredesigntechniques.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
@Slf4j
public class LocationController {

    private final LocationEndpoint locationEndpoint;

    @GetMapping
    public ResponseEntity<List<LocationDto>> getAllLocations() {
        try {
            return ResponseEntity.ok(locationEndpoint.getAll());
        } catch (Exception e) {
            log.error("Error getting all locations", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDto> getLocation(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(locationEndpoint.get(id));
        } catch (NotFoundException e) {
            log.error("Location not found: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<LocationDto> createLocation(@Valid @RequestBody LocationRequest locationRequest) {
        try {
            log.info("Creating location: {}", locationRequest);
            LocationDto locationDto = locationEndpoint.add(locationRequest);
            log.info("Location created: {}", locationDto);
            return ResponseEntity.ok(locationDto);
        } catch (Exception e) {
            log.error("Error creating location: {}", locationRequest, e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<LocationDto> updateLocation(@PathVariable Long id, @Valid @RequestBody LocationRequest locationRequest) {
        try {
            log.info("Updating location {}: {}", id, locationRequest);
            LocationDto locationDto = locationEndpoint.update(id, locationRequest);
            log.info("Location updated: {}", locationDto);
            return ResponseEntity.ok(locationDto);
        } catch (NotFoundException e) {
            log.error("Location not found for update: {}", id, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error updating location {}: {}", id, locationRequest, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        try {
            log.info("Deleting location: {}", id);
            locationEndpoint.delete(id);
            log.info("Location deleted: {}", id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            log.error("Location not found for delete: {}", id, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error deleting location: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }
} 