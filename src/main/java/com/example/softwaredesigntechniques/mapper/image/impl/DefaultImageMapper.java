package com.example.softwaredesigntechniques.mapper.image.impl;

import com.example.softwaredesigntechniques.domain.image.Image;
import com.example.softwaredesigntechniques.dto.image.ImageDto;
import com.example.softwaredesigntechniques.dto.image.ImageRequest;
import com.example.softwaredesigntechniques.mapper.image.ImageMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DefaultImageMapper implements ImageMapper {

    @Override
    @Transactional(readOnly = true)
    public ImageDto toDto(Image image) {
        return ImageDto.builder()
                .id(image.getId())
                .filePath(image.getFilePath())
                .uploadedAt(image.getUploadedAt())
                .createdAt(image.getCreatedAt())
                .updatedAt(image.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Image toImage(ImageRequest imageRequest) {
        Image image = new Image();
        image.setFilePath(imageRequest.getFilePath());
        return image;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ImageDto> toDto(Collection<Image> images) {
        Collection<ImageDto> imageDtos = new ArrayList<>();
        for (Image image : images) {
            imageDtos.add(toDto(image));
        }
        return imageDtos;
    }
} 