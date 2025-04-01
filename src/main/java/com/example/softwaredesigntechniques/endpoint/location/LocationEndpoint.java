package com.example.softwaredesigntechniques.endpoint.location;

import com.example.softwaredesigntechniques.dto.http.location.LocationDto;
import com.example.softwaredesigntechniques.dto.http.location.LocationRequest;
import com.example.softwaredesigntechniques.exception.NotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LocationEndpoint {
    @Transactional(readOnly = true)
    LocationDto get(Long id) throws NotFoundException;

    @Transactional(readOnly = true)
    List<LocationDto> getAll() throws NotFoundException;

    @Transactional(readOnly = true)
    List<LocationDto> findByAddress(String address);

    @Transactional(readOnly = true)
    List<LocationDto> findByPlaceName(String placeName);

    @Transactional(readOnly = true)
    List<LocationDto> findByLatitudeAndLongitude(Double latitude, Double longitude);

    @Transactional(readOnly = true)
    List<LocationDto> findByAddressContainingIgnoreCase(String address);

    @Transactional(readOnly = true)
    List<LocationDto> findByPlaceNameContainingIgnoreCase(String placeName);

    @Transactional
    LocationDto add(LocationRequest locationRequest);

    @Transactional
    LocationDto update(Long id, LocationRequest locationRequest) throws NotFoundException;

    @Transactional
    void delete(Long id) throws NotFoundException;
} 