package com.example.softwaredesigntechniques.endpoint.location.impl;

import com.example.softwaredesigntechniques.domain.location.Location;
import com.example.softwaredesigntechniques.dto.location.LocationDto;
import com.example.softwaredesigntechniques.dto.location.LocationRequest;
import com.example.softwaredesigntechniques.endpoint.location.LocationEndpoint;
import com.example.softwaredesigntechniques.exception.NotFoundException;
import com.example.softwaredesigntechniques.mapper.location.LocationMapper;
import com.example.softwaredesigntechniques.service.location.LocationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DefaultLocationEndpoint implements LocationEndpoint {

    private final LocationService locationService;
    private final LocationMapper locationMapper;

    @Override
    @Transactional(readOnly = true)
    public LocationDto get(Long id) throws NotFoundException {
        Location location = locationService.get(id);
        return locationMapper.toDto(location);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationDto> getAll() throws NotFoundException {
        List<Location> locations = locationService.get(List.of());
        return locations.stream()
                .map(locationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationDto> findByAddress(String address) {
        List<Location> locations = locationService.findByAddress(address);
        return locations.stream()
                .map(locationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationDto> findByPlaceName(String placeName) {
        List<Location> locations = locationService.findByPlaceName(placeName);
        return locations.stream()
                .map(locationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationDto> findByLatitudeAndLongitude(Double latitude, Double longitude) {
        List<Location> locations = locationService.findByLatitudeAndLongitude(latitude, longitude);
        return locations.stream()
                .map(locationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationDto> findByAddressContainingIgnoreCase(String address) {
        List<Location> locations = locationService.findByAddressContainingIgnoreCase(address);
        return locations.stream()
                .map(locationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationDto> findByPlaceNameContainingIgnoreCase(String placeName) {
        List<Location> locations = locationService.findByPlaceNameContainingIgnoreCase(placeName);
        return locations.stream()
                .map(locationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LocationDto add(LocationRequest locationRequest) {
        Location location = locationMapper.toLocation(locationRequest);
        location = locationService.saveOrUpdate(location);
        return locationMapper.toDto(location);
    }

    @Override
    @Transactional
    public LocationDto update(Long id, LocationRequest locationRequest) throws NotFoundException {
        Location existingLocation = locationService.get(id);
        Location updatedLocation = locationMapper.toLocation(locationRequest);
        updatedLocation.setId(existingLocation.getId());
        updatedLocation = locationService.saveOrUpdate(updatedLocation);
        return locationMapper.toDto(updatedLocation);
    }

    @Override
    @Transactional
    public void delete(Long id) throws NotFoundException {
        Location location = locationService.get(id);
        locationService.delete(location);
    }
} 