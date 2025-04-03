package com.example.softwaredesigntechniques.endpoint.event;

import com.example.softwaredesigntechniques.domain.category.Category;
import com.example.softwaredesigntechniques.dto.event.EventDto;
import com.example.softwaredesigntechniques.dto.event.EventRequest;
import com.example.softwaredesigntechniques.exception.NotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventEndpoint {
    @Transactional(readOnly = true)
    EventDto get(Long id) throws NotFoundException;

    @Transactional(readOnly = true)
    List<EventDto> getAll();

    @Transactional(readOnly = true)
    List<EventDto> findByTitle(String title);

    @Transactional(readOnly = true)
    List<EventDto> findByTitleContainingIgnoreCase(String title);

    @Transactional(readOnly = true)
    List<EventDto> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    @Transactional(readOnly = true)
    List<EventDto> findByEndTimeBetween(LocalDateTime start, LocalDateTime end);

    @Transactional(readOnly = true)
    List<EventDto> findByCapacityGreaterThan(Integer capacity);

    @Transactional(readOnly = true)
    List<EventDto> findByCategoriesContaining(Category category);

    @Transactional(readOnly = true)
    List<EventDto> findByCategoriesIn(Set<Category> categories);

    @Transactional(readOnly = true)
    List<EventDto> findByLocationId(Long locationId);

    @Transactional(readOnly = true)
    List<EventDto> findByImageId(Long imageId);

    @Transactional
    EventDto add(EventRequest eventRequest) throws NotFoundException;

    @Transactional
    EventDto update(Long id, EventRequest eventRequest) throws NotFoundException;

    @Transactional
    void delete(Long id) throws NotFoundException;
} 