package com.example.softwaredesigntechniques.repository.image;

import com.example.softwaredesigntechniques.domain.image.Image;
import com.example.softwaredesigntechniques.repository.common.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends BaseRepository<Image> {
    Optional<Image> findByFileName(String fileName);
    List<Image> findByFileNameContainingIgnoreCase(String fileName);
    boolean existsByFileName(String fileName);
} 