package com.example.softwaredesigntechniques.service.registration.impl;

import com.example.softwaredesigntechniques.domain.registration.Registration;
import com.example.softwaredesigntechniques.repository.registration.RegistrationRepository;
import com.example.softwaredesigntechniques.service.common.impl.DefaultBaseService;
import com.example.softwaredesigntechniques.service.registration.RegistrationService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DefaultRegistrationService extends DefaultBaseService<Registration, Long> implements RegistrationService {

    private final RegistrationRepository registrationRepository;

    public DefaultRegistrationService(RegistrationRepository registrationRepository) {
        super(registrationRepository);
        this.registrationRepository = registrationRepository;
    }

    @Override
    public List<Registration> findByUserId(Long userId) {
        return registrationRepository.findByUserId(userId);
    }

    @Override
    public List<Registration> findByEventId(Long eventId) {
        return registrationRepository.findByEventId(eventId);
    }

    @Override
    public Optional<Registration> findByUserIdAndEventId(Long userId, Long eventId) {
        return registrationRepository.findByUserIdAndEventId(userId, eventId);
    }

    @Override
    public List<Registration> findByRegistrationDateBetween(LocalDateTime start, LocalDateTime end) {
        return registrationRepository.findByRegistrationDateBetween(start, end);
    }

    @Override
    public boolean existsByUserIdAndEventId(Long userId, Long eventId) {
        return registrationRepository.existsByUserIdAndEventId(userId, eventId);
    }

    @Override
    public List<Registration> findByEventIdAndRegistrationDateBefore(Long eventId, LocalDateTime date) {
        return registrationRepository.findByEventIdAndRegistrationDateBefore(eventId, date);
    }

    @Override
    public List<Registration> findByEventIdAndRegistrationDateAfter(Long eventId, LocalDateTime date) {
        return registrationRepository.findByEventIdAndRegistrationDateAfter(eventId, date);
    }
} 