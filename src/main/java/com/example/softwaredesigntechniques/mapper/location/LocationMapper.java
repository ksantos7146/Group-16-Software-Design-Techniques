package com.example.softwaredesigntechniques.mapper.location;

import com.example.softwaredesigntechniques.domain.location.Location;
import com.example.softwaredesigntechniques.dto.location.LocationDto;
import com.example.softwaredesigntechniques.dto.location.LocationRequest;
import java.util.Collection;

public interface LocationMapper {
    LocationDto toDto(Location location);
    Location toLocation(LocationRequest locationRequest);
    Collection<LocationDto> toDto(Collection<Location> locations);
} 