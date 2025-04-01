package com.example.softwaredesigntechniques.repository.event;

import com.example.softwaredesigntechniques.domain.category.Category;
import com.example.softwaredesigntechniques.domain.event.Event;
import com.example.softwaredesigntechniques.repository.common.BaseRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface EventRepository extends BaseRepository<Event> {
    List<Event> findByTitle(String title);
    List<Event> findByTitleContainingIgnoreCase(String title);
    List<Event> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Event> findByEndTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Event> findByCapacityGreaterThan(Integer capacity);
    List<Event> findByCategoriesContaining(Category category);
    List<Event> findByCategoriesIn(Set<Category> categories);
    List<Event> findByLocationId(Long locationId);
    List<Event> findByImageId(Long imageId);
} 