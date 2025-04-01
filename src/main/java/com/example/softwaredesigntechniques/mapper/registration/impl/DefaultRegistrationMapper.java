package com.example.softwaredesigntechniques.mapper.registration.impl;

import com.example.softwaredesigntechniques.domain.registration.Registration;
import com.example.softwaredesigntechniques.dto.http.registration.RegistrationDto;
import com.example.softwaredesigntechniques.dto.http.registration.RegistrationRequest;
import com.example.softwaredesigntechniques.mapper.event.EventMapper;
import com.example.softwaredesigntechniques.mapper.registration.RegistrationMapper;
import com.example.softwaredesigntechniques.mapper.user.UserMapper;
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
public class DefaultRegistrationMapper implements RegistrationMapper {

    private final EventMapper eventMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public RegistrationDto toDto(Registration registration) {
        return RegistrationDto.builder()
                .id(registration.getId())
                .event(eventMapper.toDto(registration.getEvent()))
                .user(userMapper.toDto(registration.getUser()))
                .registrationDate(registration.getRegistrationDate())
                .createdAt(registration.getCreatedAt())
                .updatedAt(registration.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Registration toRegistration(RegistrationRequest registrationRequest) {
        Registration registration = new Registration();
        return registration;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<RegistrationDto> toDto(Collection<Registration> registrations) {
        Collection<RegistrationDto> registrationDtos = new ArrayList<>();
        for (Registration registration : registrations) {
            registrationDtos.add(toDto(registration));
        }
        return registrationDtos;
    }
} 