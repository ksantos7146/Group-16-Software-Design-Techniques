package com.example.softwaredesigntechniques.service.image;

import com.example.softwaredesigntechniques.domain.image.Image;
import com.example.softwaredesigntechniques.domain.location.Location;
import com.example.softwaredesigntechniques.service.common.BaseService;

import java.util.List;
import java.util.Optional;

public interface ImageService extends BaseService<Image, Long> {

    Optional<Image> findByFilePath(String filePath);

    List<Image> findByFilePathContainingIgnoreCase(String filePath);

    boolean existsByFilePath(String filePath);
}