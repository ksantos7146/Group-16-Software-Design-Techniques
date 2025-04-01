package com.example.softwaredesigntechniques.mapper.event;

import com.example.softwaredesigntechniques.domain.event.Event;

import com.example.softwaredesigntechniques.dto.http.event.EventDto;
import com.example.softwaredesigntechniques.dto.http.event.EventRequest;

import java.util.Collection;

public interface EventMapper {
    EventDto toDto(Event event);
    Event toEvent(EventRequest eventRequest);
    Collection<EventDto> toDto(Collection<Event> events);
} 