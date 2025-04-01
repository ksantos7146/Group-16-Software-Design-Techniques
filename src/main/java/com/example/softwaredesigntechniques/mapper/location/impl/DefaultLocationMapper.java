package com.example.softwaredesigntechniques.mapper.location.impl;

import com.example.softwaredesigntechniques.domain.location.Location;
import com.example.softwaredesigntechniques.dto.location.LocationDto;
import com.example.softwaredesigntechniques.dto.location.LocationRequest;
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
public class DefaultLocationMapper implements LocationMapper {

    @Override
    @Transactional(readOnly = true)
    public LocationDto toDto(Location location) {
        return LocationDto.builder()
                .id(location.getId())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .address(location.getAddress())
                .placeName(location.getPlaceName())
                .createdAt(location.getCreatedAt())
                .updatedAt(location.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Location toLocation(LocationRequest locationRequest) {
        Location location = new Location();
        location.setLatitude(locationRequest.getLatitude());
        location.setLongitude(locationRequest.getLongitude());
        location.setAddress(locationRequest.getAddress());
        location.setPlaceName(locationRequest.getPlaceName());
        return location;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<LocationDto> toDto(Collection<Location> locations) {
        Collection<LocationDto> locationDtos = new ArrayList<>();
        for (Location location : locations) {
            locationDtos.add(toDto(location));
        }
        return locationDtos;
    }
} 