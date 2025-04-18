package com.example.softwaredesigntechniques.dto.image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageRequest {
    @NotBlank
    private String fileName;
    
    @NotBlank
    private String imageData;
    
    private String contentType;
} 