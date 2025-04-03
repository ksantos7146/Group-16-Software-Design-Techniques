package com.example.softwaredesigntechniques.domain.location;

import com.example.softwaredesigntechniques.domain.common.RemovalEntity;
import com.example.softwaredesigntechniques.domain.event.Event;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "locations")
@Getter
@Setter
public class Location extends RemovalEntity {

    @NotNull
    @Column(precision = 9, scale = 6)
    private Double latitude;

    @NotNull
    @Column(precision = 9, scale = 6)
    private Double longitude;

    private String address;
    private String placeName;

    @JsonManagedReference
    @OneToMany(mappedBy = "location")
    private Set<Event> events = new HashSet<>();
} 