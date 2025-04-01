package com.example.softwaredesigntechniques.mapper.event.impl;

import com.example.softwaredesigntechniques.domain.event.Event;

import com.example.softwaredesigntechniques.dto.event.EventDto;
import com.example.softwaredesigntechniques.dto.event.EventRequest;
import com.example.softwaredesigntechniques.mapper.event.EventMapper;
import com.example.softwaredesigntechniques.mapper.image.ImageMapper;
import com.example.softwaredesigntechniques.mapper.location.LocationMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DefaultEventMapper implements EventMapper {

    private final LocationMapper locationMapper;
    private final ImageMapper imageMapper;
    private static final Logger log = LoggerFactory.getLogger(DefaultEventMapper.class);

    @Override
    @Transactional(readOnly = true)
    public EventDto toDto(Event event) {
        if (event == null) {
            log.warn("Attempted to convert null event to DTO");
            return null;
        }
        
        try {
            return EventDto.builder()
                    .id(event.getId())
                    .title(event.getTitle())
                    .description(event.getDescription())
                    .location(event.getLocation() != null ? locationMapper.toDto(event.getLocation()) : null)
                    .categories(event.getCategories())
                    .image(event.getImage() != null ? imageMapper.toDto(event.getImage()) : null)
                    .startTime(event.getStartTime())
                    .endTime(event.getEndTime())
                    .capacity(event.getCapacity())
                    .createdAt(event.getCreatedAt())
                    .updatedAt(event.getUpdatedAt())
                    .build();
        } catch (Exception e) {
            log.error("Error converting event to DTO: {}", event.getId(), e);
            // Still return a basic DTO with the ID to avoid NPEs in the UI
            return EventDto.builder()
                    .id(event.getId())
                    .title(event.getTitle() != null ? event.getTitle() : "Event with issues")
                    .description("Error loading complete event data")
                    .build();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Event toEvent(EventRequest eventRequest) {
        Event event = new Event();
        event.setTitle(eventRequest.getTitle());
        event.setDescription(eventRequest.getDescription());
        event.setCategories(eventRequest.getCategories());
        event.setStartTime(eventRequest.getStartTime());
        event.setEndTime(eventRequest.getEndTime());
        event.setCapacity(eventRequest.getCapacity());
        return event;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<EventDto> toDto(Collection<Event> events) {
        Collection<EventDto> eventDtos = new ArrayList<>();
        for (Event event : events) {
            eventDtos.add(toDto(event));
        }
        return eventDtos;
    }
} 