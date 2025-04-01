package com.example.softwaredesigntechniques.domain.event;

import com.example.softwaredesigntechniques.domain.category.Category;
import com.example.softwaredesigntechniques.domain.common.RemovalEntity;
import com.example.softwaredesigntechniques.domain.image.Image;
import com.example.softwaredesigntechniques.domain.location.Location;
import com.example.softwaredesigntechniques.domain.registration.Registration;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "events")
@Getter
@Setter
public class Event extends RemovalEntity {

    @NotBlank
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @NotNull
    @Column(name = "start_time")
    private LocalDateTime startTime;

    @NotNull
    @Column(name = "end_time")
    private LocalDateTime endTime;

    private Integer capacity;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @ElementCollection
    @CollectionTable(
        name = "event_categories",
        joinColumns = @JoinColumn(name = "event_id")
    )
    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "event")
    private Set<Registration> registrations = new HashSet<>();
} 