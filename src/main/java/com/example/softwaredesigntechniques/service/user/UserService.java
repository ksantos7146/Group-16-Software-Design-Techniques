package com.example.softwaredesigntechniques.service.user;

import com.example.softwaredesigntechniques.domain.user.User;
import com.example.softwaredesigntechniques.service.common.BaseService;

import java.util.List;
import java.util.Optional;

public interface UserService extends BaseService<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByFirstName(String firstName);
    List<User> findByLastName(String lastName);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
} 