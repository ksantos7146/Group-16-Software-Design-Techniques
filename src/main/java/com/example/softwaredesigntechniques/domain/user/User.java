package com.example.softwaredesigntechniques.domain.user;

import com.example.softwaredesigntechniques.domain.common.RemovalEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends RemovalEntity {

    @NotBlank
    @Column(unique = true)
    private String username;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    private String password;

    private String firstName;
    private String lastName;

    @OneToMany(mappedBy = "createdBy")
    private Set<User> createdUsers = new HashSet<>();

    @OneToMany(mappedBy = "updatedBy")
    private Set<User> updatedUsers = new HashSet<>();

    @OneToMany(mappedBy = "deletedBy")
    private Set<User> deletedUsers = new HashSet<>();
} 