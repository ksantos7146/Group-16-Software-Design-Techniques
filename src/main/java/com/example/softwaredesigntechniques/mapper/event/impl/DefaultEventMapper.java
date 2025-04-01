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

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DefaultEventMapper implements EventMapper {

    private final LocationMapper locationMapper;
    private final ImageMapper imageMapper;

    @Override
    @Transactional(readOnly = true)
    public EventDto toDto(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .location(locationMapper.toDto(event.getLocation()))
                .categories(event.getCategories())
                .image(imageMapper.toDto(event.getImage()))
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .capacity(event.getCapacity())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .build();
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