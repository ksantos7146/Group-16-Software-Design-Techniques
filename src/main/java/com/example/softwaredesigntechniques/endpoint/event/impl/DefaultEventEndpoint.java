package com.example.softwaredesigntechniques.endpoint.event.impl;

import com.example.softwaredesigntechniques.domain.category.Category;
import com.example.softwaredesigntechniques.domain.event.Event;
import com.example.softwaredesigntechniques.domain.location.Location;
import com.example.softwaredesigntechniques.domain.image.Image;
import com.example.softwaredesigntechniques.dto.event.EventDto;
import com.example.softwaredesigntechniques.dto.event.EventRequest;
import com.example.softwaredesigntechniques.endpoint.event.EventEndpoint;
import com.example.softwaredesigntechniques.exception.NotFoundException;
import com.example.softwaredesigntechniques.mapper.event.EventMapper;
import com.example.softwaredesigntechniques.service.event.EventService;
import com.example.softwaredesigntechniques.service.location.LocationService;
import com.example.softwaredesigntechniques.service.image.ImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DefaultEventEndpoint implements EventEndpoint {

    private final EventService eventService;
    private final EventMapper eventMapper;
    private final LocationService locationService;
    private final ImageService imageService;

    @Override
    @Transactional(readOnly = true)
    public EventDto get(Long id) throws NotFoundException {
        Event event = eventService.get(id);
        return eventMapper.toDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDto> getAll() throws NotFoundException {
        log.info("Getting all events from database");
        List<Event> events = eventService.findAll();
        log.info("Found {} raw events from database", events.size());
        
        // Debug log each event
        for (Event event : events) {
            log.info("Event from DB: id={}, title={}, startTime={}, endTime={}, location={}", 
                event.getId(), event.getTitle(), event.getStartTime(), event.getEndTime(), 
                (event.getLocation() != null ? event.getLocation().getId() : "null"));
        }
        
        List<EventDto> eventDtos = events.stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
        
        log.info("Converted {} events to DTOs", eventDtos.size());
        return eventDtos;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDto> findByTitle(String title) {
        List<Event> events = eventService.findByTitle(title);
        return events.stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDto> findByTitleContainingIgnoreCase(String title) {
        List<Event> events = eventService.findByTitleContainingIgnoreCase(title);
        return events.stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDto> findByStartTimeBetween(LocalDateTime start, LocalDateTime end) {
        List<Event> events = eventService.findByStartTimeBetween(start, end);
        return events.stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDto> findByEndTimeBetween(LocalDateTime start, LocalDateTime end) {
        List<Event> events = eventService.findByEndTimeBetween(start, end);
        return events.stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDto> findByCapacityGreaterThan(Integer capacity) {
        List<Event> events = eventService.findByCapacityGreaterThan(capacity);
        return events.stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDto> findByCategoriesContaining(Category category) {
        List<Event> events = eventService.findByCategoriesContaining(category);
        return events.stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDto> findByCategoriesIn(Set<Category> categories) {
        List<Event> events = eventService.findByCategoriesIn(categories);
        return events.stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDto> findByLocationId(Long locationId) {
        List<Event> events = eventService.findByLocationId(locationId);
        return events.stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDto> findByImageId(Long imageId) {
        List<Event> events = eventService.findByImageId(imageId);
        return events.stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventDto add(EventRequest eventRequest) throws NotFoundException {
        try {
            log.debug("Creating event from request: {}", eventRequest);
            Event event = eventMapper.toEvent(eventRequest);
            
            // Ensure categories are set properly
            if (eventRequest.getCategories() == null || eventRequest.getCategories().isEmpty()) {
                // Set default category if none provided
                event.setCategories(Set.of(Category.OTHER));
                log.debug("No categories provided, using default OTHER category");
            }
            
            // Handle location
            if (eventRequest.getLocationId() != null) {
                // Use existing location
                Location location = locationService.get(eventRequest.getLocationId());
                event.setLocation(location);
                log.debug("Using existing location with ID: {}", location.getId());
            } else if (eventRequest.getLocationRequest() != null) {
                // Create a new location
                Location location = new Location();
                location.setLatitude(eventRequest.getLocationRequest().getLatitude());
                location.setLongitude(eventRequest.getLocationRequest().getLongitude());
                location.setAddress(eventRequest.getLocationRequest().getAddress());
                location.setPlaceName(eventRequest.getLocationRequest().getPlaceName());
                
                location = locationService.saveOrUpdate(location);
                event.setLocation(location);
                log.info("Created new location {} for event", location.getId());
            } else {
                throw new IllegalArgumentException("Either locationId or locationRequest must be provided");
            }
            
            // Set image if provided
            if (eventRequest.getImageId() != null) {
                try {
                    Image image = imageService.get(eventRequest.getImageId());
                    if (image != null) {
                        event.setImage(image);
                        log.debug("Set image with ID: {}", image.getId());
                    } else {
                        log.warn("Image with ID {} not found, continuing without image", eventRequest.getImageId());
                    }
                } catch (Exception e) {
                    log.warn("Error fetching image with ID {}, continuing without image: {}", eventRequest.getImageId(), e.getMessage());
                }
            }
            
            // Validate event times but allow invalid ones (with warning)
            if (event.getStartTime() != null && event.getEndTime() != null) {
                if (event.getStartTime().isAfter(event.getEndTime())) {
                    log.warn("Event has invalid dates: start time {} is after end time {}", 
                            event.getStartTime(), event.getEndTime());
                }
            }
            
            event = eventService.saveOrUpdate(event);
            log.info("Successfully created event with ID: {}", event.getId());
            return eventMapper.toDto(event);
        } catch (Exception e) {
            log.error("Error adding event: {}", eventRequest, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public EventDto update(Long id, EventRequest eventRequest) throws NotFoundException {
        Event existingEvent = eventService.get(id);
        Event updatedEvent = eventMapper.toEvent(eventRequest);
        updatedEvent.setId(existingEvent.getId());
        
        // Set location
        if (eventRequest.getLocationId() != null) {
            try {
                Location location = locationService.get(eventRequest.getLocationId());
                updatedEvent.setLocation(location);
            } catch (Exception e) {
                log.error("Error fetching location with ID {}: {}", eventRequest.getLocationId(), e.getMessage());
                throw e;
            }
        } else if (eventRequest.getLocationRequest() != null) {
            // Create a new location
            Location location = new Location();
            location.setLatitude(eventRequest.getLocationRequest().getLatitude());
            location.setLongitude(eventRequest.getLocationRequest().getLongitude());
            location.setAddress(eventRequest.getLocationRequest().getAddress());
            location.setPlaceName(eventRequest.getLocationRequest().getPlaceName());
            
            location = locationService.saveOrUpdate(location);
            updatedEvent.setLocation(location);
            log.info("Created new location {} for updated event", location.getId());
        }
        
        // Set image if provided
        if (eventRequest.getImageId() != null) {
            try {
                Image image = imageService.get(eventRequest.getImageId());
                if (image != null) {
                    updatedEvent.setImage(image);
                    log.debug("Set image with ID: {}", image.getId());
                } else {
                    log.warn("Image with ID {} not found, continuing without image", eventRequest.getImageId());
                }
            } catch (Exception e) {
                log.warn("Error fetching image with ID {}, continuing without image: {}", eventRequest.getImageId(), e.getMessage());
            }
        }
        
        // Check for date issues but allow them (just log a warning)
        if (updatedEvent.getStartTime() != null && updatedEvent.getEndTime() != null) {
            if (updatedEvent.getStartTime().isAfter(updatedEvent.getEndTime())) {
                log.warn("Event has invalid dates: start time {} is after end time {}", 
                         updatedEvent.getStartTime(), updatedEvent.getEndTime());
            }
        }
        
        updatedEvent = eventService.saveOrUpdate(updatedEvent);
        return eventMapper.toDto(updatedEvent);
    }

    @Override
    @Transactional
    public void delete(Long id) throws NotFoundException {
        Event event = eventService.get(id);
        eventService.delete(event);
    }
} 