package com.example.softwaredesigntechniques.service.image.impl;

import com.example.softwaredesigntechniques.domain.image.Image;
import com.example.softwaredesigntechniques.repository.image.ImageRepository;
import com.example.softwaredesigntechniques.service.common.impl.DefaultBaseService;
import com.example.softwaredesigntechniques.service.image.ImageService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DefaultImageService extends DefaultBaseService<Image, Long> implements ImageService {

    private final ImageRepository imageRepository;

    public DefaultImageService(ImageRepository imageRepository) {
        super(imageRepository);
        this.imageRepository = imageRepository;
    }

    @Override
    public Optional<Image> findByFileName(String fileName) {
        return imageRepository.findByFileName(fileName);
    }

    @Override
    public List<Image> findByFileNameContainingIgnoreCase(String fileName) {
        return imageRepository.findByFileNameContainingIgnoreCase(fileName);
    }

    @Override
    public boolean existsByFileName(String fileName) {
        return imageRepository.existsByFileName(fileName);
    }
} 