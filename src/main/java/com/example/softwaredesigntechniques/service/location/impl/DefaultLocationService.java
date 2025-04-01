package com.example.softwaredesigntechniques.service.location.impl;

import com.example.softwaredesigntechniques.domain.location.Location;
import com.example.softwaredesigntechniques.repository.location.LocationRepository;
import com.example.softwaredesigntechniques.service.common.impl.DefaultBaseService;
import com.example.softwaredesigntechniques.service.location.LocationService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DefaultLocationService extends DefaultBaseService<Location, Long> implements LocationService {

    private final LocationRepository locationRepository;

    public DefaultLocationService(LocationRepository locationRepository) {
        super(locationRepository);
        this.locationRepository = locationRepository;
    }

    @Override
    public List<Location> findByAddress(String address) {
        return locationRepository.findByAddress(address);
    }

    @Override
    public List<Location> findByPlaceName(String placeName) {
        return locationRepository.findByPlaceName(placeName);
    }

    @Override
    public List<Location> findByLatitudeAndLongitude(Double latitude, Double longitude) {
        return locationRepository.findByLatitudeAndLongitude(latitude, longitude);
    }

    @Override
    public List<Location> findByAddressContainingIgnoreCase(String address) {
        return locationRepository.findByAddressContainingIgnoreCase(address);
    }

    @Override
    public List<Location> findByPlaceNameContainingIgnoreCase(String placeName) {
        return locationRepository.findByPlaceNameContainingIgnoreCase(placeName);
    }
} 