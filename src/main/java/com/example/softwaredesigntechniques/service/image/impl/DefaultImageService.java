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
    public Optional<Image> findByFilePath(String filePath) {
        return imageRepository.findByFilePath(filePath);
    }

    @Override
    public List<Image> findByFilePathContainingIgnoreCase(String filePath) {
        return imageRepository.findByFilePathContainingIgnoreCase(filePath);
    }

    @Override
    public boolean existsByFilePath(String filePath) {
        return imageRepository.existsByFilePath(filePath);
    }
} 