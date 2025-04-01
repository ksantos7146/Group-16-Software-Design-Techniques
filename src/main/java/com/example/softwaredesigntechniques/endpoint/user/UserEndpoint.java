package com.example.softwaredesigntechniques.endpoint.user;

import com.example.softwaredesigntechniques.dto.user.UserDto;
import com.example.softwaredesigntechniques.dto.user.UserRequest;
import com.example.softwaredesigntechniques.dto.user.LoginRequest;
import com.example.softwaredesigntechniques.exception.NotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserEndpoint {
    @Transactional(readOnly = true)
    UserDto get(Long id) throws NotFoundException;

    @Transactional(readOnly = true)
    List<UserDto> getAll() throws NotFoundException;

    @Transactional(readOnly = true)
    UserDto findByUsername(String username) throws NotFoundException;

    @Transactional(readOnly = true)
    UserDto findByEmail(String email) throws NotFoundException;

    @Transactional(readOnly = true)
    List<UserDto> findByFirstName(String firstName);

    @Transactional(readOnly = true)
    List<UserDto> findByLastName(String lastName);

    @Transactional
    UserDto add(UserRequest userRequest);

    @Transactional
    UserDto update(Long id, UserRequest userRequest) throws NotFoundException;

    @Transactional
    void delete(Long id) throws NotFoundException;

    @Transactional(readOnly = true)
    UserDto login(LoginRequest loginRequest) throws NotFoundException;
} 