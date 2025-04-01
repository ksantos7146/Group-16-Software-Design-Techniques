package com.example.softwaredesigntechniques.endpoint.image;

import com.example.softwaredesigntechniques.dto.image.ImageDto;
import com.example.softwaredesigntechniques.dto.image.ImageRequest;
import com.example.softwaredesigntechniques.exception.NotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ImageEndpoint {
    @Transactional(readOnly = true)
    ImageDto get(Long id) throws NotFoundException;

    @Transactional(readOnly = true)
    List<ImageDto> getAll() throws NotFoundException;

    @Transactional(readOnly = true)
    Optional<ImageDto> findByFilePath(String filePath);

    @Transactional(readOnly = true)
    List<ImageDto> findByFilePathContainingIgnoreCase(String filePath);

    @Transactional(readOnly = true)
    boolean existsByFilePath(String filePath);

    @Transactional
    ImageDto add(ImageRequest imageRequest);

    @Transactional
    ImageDto update(Long id, ImageRequest imageRequest) throws NotFoundException;

    @Transactional
    void delete(Long id) throws NotFoundException;
} 