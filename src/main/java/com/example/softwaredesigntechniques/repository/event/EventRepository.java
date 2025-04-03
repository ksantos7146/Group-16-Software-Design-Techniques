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
    List<Event> findByTitleAndDeletedByIsNull(String title);
    List<Event> findByTitleContainingIgnoreCaseAndDeletedByIsNull(String title);
    List<Event> findByStartTimeBetweenAndDeletedByIsNull(LocalDateTime start, LocalDateTime end);
    List<Event> findByEndTimeBetweenAndDeletedByIsNull(LocalDateTime start, LocalDateTime end);
    List<Event> findByCapacityGreaterThanAndDeletedByIsNull(Integer capacity);
    List<Event> findByCategoriesContainingAndDeletedByIsNull(Category category);
    List<Event> findByCategoriesInAndDeletedByIsNull(Set<Category> categories);
    List<Event> findByLocationIdAndDeletedByIsNull(Long locationId);
    List<Event> findByImageIdAndDeletedByIsNull(Long imageId);
    List<Event> findByDeletedByIsNull();
} 