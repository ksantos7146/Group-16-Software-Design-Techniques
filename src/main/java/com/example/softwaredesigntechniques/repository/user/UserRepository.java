package com.example.softwaredesigntechniques.repository.user;

import com.example.softwaredesigntechniques.domain.user.User;
import com.example.softwaredesigntechniques.repository.common.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByFirstName(String firstName);
    List<User> findByLastName(String lastName);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
} 