package com.example.softwaredesigntechniques.service.location;

import com.example.softwaredesigntechniques.domain.location.Location;
import com.example.softwaredesigntechniques.service.common.BaseService;

import java.util.List;

public interface LocationService extends BaseService<Location, Long> {
    List<Location> findByAddress(String address);
    List<Location> findByPlaceName(String placeName);
    List<Location> findByLatitudeAndLongitude(Double latitude, Double longitude);
    List<Location> findByAddressContainingIgnoreCase(String address);
    List<Location> findByPlaceNameContainingIgnoreCase(String placeName);
} 