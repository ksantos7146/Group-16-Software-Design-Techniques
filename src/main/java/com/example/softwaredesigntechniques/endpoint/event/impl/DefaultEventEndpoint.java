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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
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
        List<Event> events = eventService.get(List.of());
        return events.stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
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
        Event event = eventMapper.toEvent(eventRequest);
        
        // Set location
        Location location = locationService.get(eventRequest.getLocationId());
        event.setLocation(location);
        
        // Set image if provided
        if (eventRequest.getImageId() != null) {
            Image image = imageService.get(eventRequest.getImageId());
            event.setImage(image);
        }
        
        event = eventService.saveOrUpdate(event);
        return eventMapper.toDto(event);
    }

    @Override
    @Transactional
    public EventDto update(Long id, EventRequest eventRequest) throws NotFoundException {
        Event existingEvent = eventService.get(id);
        Event updatedEvent = eventMapper.toEvent(eventRequest);
        updatedEvent.setId(existingEvent.getId());
        
        // Set location
        Location location = locationService.get(eventRequest.getLocationId());
        updatedEvent.setLocation(location);
        
        // Set image if provided
        if (eventRequest.getImageId() != null) {
            Image image = imageService.get(eventRequest.getImageId());
            updatedEvent.setImage(image);
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