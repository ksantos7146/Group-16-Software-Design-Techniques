package com.example.softwaredesigntechniques.dto.http.location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationRequest {
    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    private String address;
    private String placeName;
} 