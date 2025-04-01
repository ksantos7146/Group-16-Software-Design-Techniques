package com.example.softwaredesigntechniques.domain.registration;

import com.example.softwaredesigntechniques.domain.common.RemovalEntity;
import com.example.softwaredesigntechniques.domain.event.Event;
import com.example.softwaredesigntechniques.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "registrations")
@Getter
@Setter
public class Registration extends RemovalEntity {

    @ManyToOne
    @JoinColumn(name = "event_id")
    @NotNull
    private Event event;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;
} 