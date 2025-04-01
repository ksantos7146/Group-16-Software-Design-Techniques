package com.example.softwaredesigntechniques.endpoint.image.impl;

import com.example.softwaredesigntechniques.domain.image.Image;
import com.example.softwaredesigntechniques.dto.image.ImageDto;
import com.example.softwaredesigntechniques.dto.image.ImageRequest;
import com.example.softwaredesigntechniques.endpoint.image.ImageEndpoint;
import com.example.softwaredesigntechniques.exception.NotFoundException;
import com.example.softwaredesigntechniques.mapper.image.ImageMapper;
import com.example.softwaredesigntechniques.service.image.ImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DefaultImageEndpoint implements ImageEndpoint {

    private final ImageService imageService;
    private final ImageMapper imageMapper;

    @Override
    @Transactional(readOnly = true)
    public ImageDto get(Long id) throws NotFoundException {
        Image image = imageService.get(id);
        return imageMapper.toDto(image);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImageDto> getAll() throws NotFoundException {
        List<Image> images = imageService.get(List.of());
        return images.stream()
                .map(imageMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ImageDto> findByFileName(String fileName) {
        return imageService.findByFileName(fileName)
                .map(imageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImageDto> findByFileNameContainingIgnoreCase(String fileName) {
        List<Image> images = imageService.findByFileNameContainingIgnoreCase(fileName);
        return new ArrayList<>(imageMapper.toDto(images));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByFileName(String fileName) {
        return imageService.existsByFileName(fileName);
    }

    @Override
    @Transactional
    public ImageDto add(ImageRequest imageRequest) {
        Image image = imageMapper.toImage(imageRequest);
        image = imageService.saveOrUpdate(image);
        return imageMapper.toDto(image);
    }

    @Override
    @Transactional
    public ImageDto update(Long id, ImageRequest imageRequest) throws NotFoundException {
        Image existingImage = imageService.get(id);
        Image updatedImage = imageMapper.toImage(imageRequest);
        updatedImage.setId(existingImage.getId());
        updatedImage = imageService.saveOrUpdate(updatedImage);
        return imageMapper.toDto(updatedImage);
    }

    @Override
    @Transactional
    public void delete(Long id) throws NotFoundException {
        Image image = imageService.get(id);
        imageService.delete(image);
    }
} 