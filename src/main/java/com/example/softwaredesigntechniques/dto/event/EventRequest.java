package com.example.softwaredesigntechniques.dto.event;

import com.example.softwaredesigntechniques.domain.category.Category;
import com.example.softwaredesigntechniques.dto.location.LocationRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {
    @NotBlank
    private String title;
    private String description;
    
    // Either locationId or locationRequest should be provided
    private Long locationId;
    
    // Optional embedded location information
    private LocationRequest locationRequest;
    
    private Set<Category> categories;
    private Long imageId;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;
    private Integer capacity;
} 