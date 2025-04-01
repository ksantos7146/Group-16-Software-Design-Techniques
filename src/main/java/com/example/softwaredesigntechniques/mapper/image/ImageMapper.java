package com.example.softwaredesigntechniques.mapper.image;

import com.example.softwaredesigntechniques.domain.image.Image;
import com.example.softwaredesigntechniques.dto.image.ImageDto;
import com.example.softwaredesigntechniques.dto.image.ImageRequest;

import java.util.Collection;

public interface ImageMapper {
    ImageDto toDto(Image image);
    Image toImage(ImageRequest imageRequest);
    Collection<ImageDto> toDto(Collection<Image> images);
} 