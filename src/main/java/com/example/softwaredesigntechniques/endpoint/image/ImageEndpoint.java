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
    Optional<ImageDto> findByFileName(String fileName);

    @Transactional(readOnly = true)
    List<ImageDto> findByFileNameContainingIgnoreCase(String fileName);

    @Transactional(readOnly = true)
    boolean existsByFileName(String fileName);

    @Transactional
    ImageDto add(ImageRequest imageRequest);

    @Transactional
    ImageDto update(Long id, ImageRequest imageRequest) throws NotFoundException;

    @Transactional
    void delete(Long id) throws NotFoundException;
} 