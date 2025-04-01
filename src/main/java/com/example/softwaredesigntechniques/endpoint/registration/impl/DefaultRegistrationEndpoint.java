package com.example.softwaredesigntechniques.endpoint.registration.impl;

import com.example.softwaredesigntechniques.domain.event.Event;
import com.example.softwaredesigntechniques.domain.registration.Registration;
import com.example.softwaredesigntechniques.domain.user.User;
import com.example.softwaredesigntechniques.dto.registration.RegistrationDto;
import com.example.softwaredesigntechniques.dto.registration.RegistrationRequest;
import com.example.softwaredesigntechniques.endpoint.registration.RegistrationEndpoint;
import com.example.softwaredesigntechniques.exception.NotFoundException;
import com.example.softwaredesigntechniques.mapper.registration.RegistrationMapper;
import com.example.softwaredesigntechniques.service.event.EventService;
import com.example.softwaredesigntechniques.service.registration.RegistrationService;
import com.example.softwaredesigntechniques.service.user.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DefaultRegistrationEndpoint implements RegistrationEndpoint {

    private final RegistrationService registrationService;
    private final RegistrationMapper registrationMapper;
    private final EventService eventService;
    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public RegistrationDto get(Long id) throws NotFoundException {
        Registration registration = registrationService.get(id);
        return registrationMapper.toDto(registration);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegistrationDto> getAll() throws NotFoundException {
        List<Registration> registrations = registrationService.get(List.of());
        return registrations.stream()
                .map(registrationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegistrationDto> findByUserId(Long userId) {
        List<Registration> registrations = registrationService.findByUserId(userId);
        return registrations.stream()
                .map(registrationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegistrationDto> findByEventId(Long eventId) {
        List<Registration> registrations = registrationService.findByEventId(eventId);
        return registrations.stream()
                .map(registrationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RegistrationDto> findByUserIdAndEventId(Long userId, Long eventId) {
        return registrationService.findByUserIdAndEventId(userId, eventId)
                .map(registrationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegistrationDto> findByRegistrationDateBetween(LocalDateTime start, LocalDateTime end) {
        List<Registration> registrations = registrationService.findByRegistrationDateBetween(start, end);
        return registrations.stream()
                .map(registrationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUserIdAndEventId(Long userId, Long eventId) {
        return registrationService.existsByUserIdAndEventId(userId, eventId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegistrationDto> findByEventIdAndRegistrationDateBefore(Long eventId, LocalDateTime date) {
        List<Registration> registrations = registrationService.findByEventIdAndRegistrationDateBefore(eventId, date);
        return registrations.stream()
                .map(registrationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegistrationDto> findByEventIdAndRegistrationDateAfter(Long eventId, LocalDateTime date) {
        List<Registration> registrations = registrationService.findByEventIdAndRegistrationDateAfter(eventId, date);
        return registrations.stream()
                .map(registrationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RegistrationDto add(RegistrationRequest registrationRequest) throws NotFoundException {
        Registration registration = registrationMapper.toRegistration(registrationRequest);
        
        // Set user
        User user = userService.get(registrationRequest.getUserId());
        registration.setUser(user);
        
        // Set event
        Event event = eventService.get(registrationRequest.getEventId());
        registration.setEvent(event);
        
        registration = registrationService.saveOrUpdate(registration);
        return registrationMapper.toDto(registration);
    }

    @Override
    @Transactional
    public RegistrationDto update(Long id, RegistrationRequest registrationRequest) throws NotFoundException {
        Registration existingRegistration = registrationService.get(id);
        Registration updatedRegistration = registrationMapper.toRegistration(registrationRequest);
        updatedRegistration.setId(existingRegistration.getId());
        
        // Set user
        User user = userService.get(registrationRequest.getUserId());
        updatedRegistration.setUser(user);
        
        // Set event
        Event event = eventService.get(registrationRequest.getEventId());
        updatedRegistration.setEvent(event);
        
        updatedRegistration = registrationService.saveOrUpdate(updatedRegistration);
        return registrationMapper.toDto(updatedRegistration);
    }

    @Override
    @Transactional
    public void delete(Long id) throws NotFoundException {
        Registration registration = registrationService.get(id);
        registrationService.delete(registration);
    }
} 