package com.example.softwaredesigntechniques.endpoint.registration;

import com.example.softwaredesigntechniques.dto.http.registration.RegistrationDto;
import com.example.softwaredesigntechniques.dto.http.registration.RegistrationRequest;
import com.example.softwaredesigntechniques.exception.NotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RegistrationEndpoint {
    @Transactional(readOnly = true)
    RegistrationDto get(Long id) throws NotFoundException;

    @Transactional(readOnly = true)
    List<RegistrationDto> getAll() throws NotFoundException;

    @Transactional(readOnly = true)
    List<RegistrationDto> findByUserId(Long userId);

    @Transactional(readOnly = true)
    List<RegistrationDto> findByEventId(Long eventId);

    @Transactional(readOnly = true)
    Optional<RegistrationDto> findByUserIdAndEventId(Long userId, Long eventId);

    @Transactional(readOnly = true)
    List<RegistrationDto> findByRegistrationDateBetween(LocalDateTime start, LocalDateTime end);

    @Transactional(readOnly = true)
    boolean existsByUserIdAndEventId(Long userId, Long eventId);

    @Transactional(readOnly = true)
    List<RegistrationDto> findByEventIdAndRegistrationDateBefore(Long eventId, LocalDateTime date);

    @Transactional(readOnly = true)
    List<RegistrationDto> findByEventIdAndRegistrationDateAfter(Long eventId, LocalDateTime date);

    @Transactional
    RegistrationDto add(RegistrationRequest registrationRequest) throws NotFoundException;

    @Transactional
    RegistrationDto update(Long id, RegistrationRequest registrationRequest) throws NotFoundException;

    @Transactional
    void delete(Long id) throws NotFoundException;
} 