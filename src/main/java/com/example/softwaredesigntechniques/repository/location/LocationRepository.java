package com.example.softwaredesigntechniques.repository.location;

import com.example.softwaredesigntechniques.domain.location.Location;
import com.example.softwaredesigntechniques.repository.common.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends BaseRepository<Location> {
    List<Location> findByAddress(String address);
    List<Location> findByPlaceName(String placeName);
    List<Location> findByLatitudeAndLongitude(Double latitude, Double longitude);
    List<Location> findByAddressContainingIgnoreCase(String address);
    List<Location> findByPlaceNameContainingIgnoreCase(String placeName);
} 