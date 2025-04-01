package com.example.softwaredesigntechniques.service.registration;

import com.example.softwaredesigntechniques.domain.registration.Registration;
import com.example.softwaredesigntechniques.service.common.BaseService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RegistrationService extends BaseService<Registration, Long> {
    List<Registration> findByUserId(Long userId);
    List<Registration> findByEventId(Long eventId);
    Optional<Registration> findByUserIdAndEventId(Long userId, Long eventId);
    List<Registration> findByRegistrationDateBetween(LocalDateTime start, LocalDateTime end);
    boolean existsByUserIdAndEventId(Long userId, Long eventId);
    List<Registration> findByEventIdAndRegistrationDateBefore(Long eventId, LocalDateTime date);
    List<Registration> findByEventIdAndRegistrationDateAfter(Long eventId, LocalDateTime date);
} 