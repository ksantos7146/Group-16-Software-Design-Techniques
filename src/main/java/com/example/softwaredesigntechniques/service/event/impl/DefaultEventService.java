package com.example.softwaredesigntechniques.service.event.impl;

import com.example.softwaredesigntechniques.domain.category.Category;
import com.example.softwaredesigntechniques.domain.event.Event;
import com.example.softwaredesigntechniques.repository.event.EventRepository;
import com.example.softwaredesigntechniques.service.common.impl.DefaultBaseService;
import com.example.softwaredesigntechniques.service.event.EventService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DefaultEventService extends DefaultBaseService<Event, Long> implements EventService {

    private final EventRepository eventRepository;

    public DefaultEventService(EventRepository eventRepository) {
        super(eventRepository);
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Event> findByTitle(String title) {
        return eventRepository.findByTitle(title);
    }

    @Override
    public List<Event> findByTitleContainingIgnoreCase(String title) {
        return eventRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public List<Event> findByStartTimeBetween(LocalDateTime start, LocalDateTime end) {
        return eventRepository.findByStartTimeBetween(start, end);
    }

    @Override
    public List<Event> findByEndTimeBetween(LocalDateTime start, LocalDateTime end) {
        return eventRepository.findByEndTimeBetween(start, end);
    }

    @Override
    public List<Event> findByCapacityGreaterThan(Integer capacity) {
        return eventRepository.findByCapacityGreaterThan(capacity);
    }

    @Override
    public List<Event> findByCategoriesContaining(Category category) {
        return eventRepository.findByCategoriesContaining(category);
    }

    @Override
    public List<Event> findByCategoriesIn(Set<Category> categories) {
        return eventRepository.findByCategoriesIn(categories);
    }

    @Override
    public List<Event> findByLocationId(Long locationId) {
        return eventRepository.findByLocationId(locationId);
    }

    @Override
    public List<Event> findByImageId(Long imageId) {
        return eventRepository.findByImageId(imageId);
    }

    @Override
    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public EventRepository getEventRepository() {
        return eventRepository;
    }
} 