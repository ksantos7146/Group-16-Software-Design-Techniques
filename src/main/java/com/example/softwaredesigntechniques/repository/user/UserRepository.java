package com.example.softwaredesigntechniques.repository.user;

import com.example.softwaredesigntechniques.domain.user.User;
import com.example.softwaredesigntechniques.repository.common.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User> {
    Optional<User> findByUsernameAndDeletedByIsNull(String username);
    Optional<User> findByEmailAndDeletedByIsNull(String email);
    List<User> findByFirstNameAndDeletedByIsNull(String firstName);
    List<User> findByLastNameAndDeletedByIsNull(String lastName);
    boolean existsByUsernameAndDeletedByIsNull(String username);
    boolean existsByEmailAndDeletedByIsNull(String email);
} 