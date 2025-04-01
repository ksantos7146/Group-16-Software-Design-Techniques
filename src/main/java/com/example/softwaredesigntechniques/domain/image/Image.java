package com.example.softwaredesigntechniques.domain.image;

import com.example.softwaredesigntechniques.domain.common.RemovalEntity;
import com.example.softwaredesigntechniques.domain.event.Event;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "images")
@Getter
@Setter
public class Image extends RemovalEntity {

    @NotBlank
    @Column(name = "file_name")
    private String fileName;
    
    @Lob
    @Column(name = "image_data", columnDefinition = "LONGTEXT")
    private String imageData;
    
    @Column(name = "content_type")
    private String contentType;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @OneToOne(mappedBy = "image")
    private Event event;
} 