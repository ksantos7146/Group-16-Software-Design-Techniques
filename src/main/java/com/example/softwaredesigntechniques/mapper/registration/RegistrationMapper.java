package com.example.softwaredesigntechniques.mapper.registration;

import com.example.softwaredesigntechniques.domain.registration.Registration;
import com.example.softwaredesigntechniques.dto.registration.RegistrationDto;
import com.example.softwaredesigntechniques.dto.registration.RegistrationRequest;

import java.util.Collection;

public interface RegistrationMapper {
    RegistrationDto toDto(Registration registration);
    Registration toRegistration(RegistrationRequest registrationRequest);
    Collection<RegistrationDto> toDto(Collection<Registration> registrations);
} 