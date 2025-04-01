package com.example.softwaredesigntechniques.repository.registration;

import com.example.softwaredesigntechniques.domain.registration.Registration;
import com.example.softwaredesigntechniques.repository.common.BaseRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends BaseRepository<Registration> {
    List<Registration> findByUserId(Long userId);
    List<Registration> findByEventId(Long eventId);
    Optional<Registration> findByUserIdAndEventId(Long userId, Long eventId);
    List<Registration> findByRegistrationDateBetween(LocalDateTime start, LocalDateTime end);
    boolean existsByUserIdAndEventId(Long userId, Long eventId);
    List<Registration> findByEventIdAndRegistrationDateBefore(Long eventId, LocalDateTime date);
    List<Registration> findByEventIdAndRegistrationDateAfter(Long eventId, LocalDateTime date);
} 