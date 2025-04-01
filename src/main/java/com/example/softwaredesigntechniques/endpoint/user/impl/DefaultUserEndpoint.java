package com.example.softwaredesigntechniques.endpoint.user.impl;

import com.example.softwaredesigntechniques.domain.user.User;
import com.example.softwaredesigntechniques.dto.http.user.UserDto;
import com.example.softwaredesigntechniques.dto.http.user.UserRequest;
import com.example.softwaredesigntechniques.endpoint.user.UserEndpoint;
import com.example.softwaredesigntechniques.exception.NotFoundException;
import com.example.softwaredesigntechniques.mapper.user.UserMapper;
import com.example.softwaredesigntechniques.service.user.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DefaultUserEndpoint implements UserEndpoint {

    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public UserDto get(Long id) throws NotFoundException {
        User user = userService.get(id);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAll() throws NotFoundException {
        List<User> users = userService.get(List.of());
        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findByUsername(String username) throws NotFoundException {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found with username: " + username));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findByEmail(String email) throws NotFoundException {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findByFirstName(String firstName) {
        List<User> users = userService.findByFirstName(firstName);
        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findByLastName(String lastName) {
        List<User> users = userService.findByLastName(lastName);
        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto add(UserRequest userRequest) {
        User user = userMapper.toUser(userRequest);
        user = userService.saveOrUpdate(user);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto update(Long id, UserRequest userRequest) throws NotFoundException {
        User existingUser = userService.get(id);
        User updatedUser = userMapper.toUser(userRequest);
        updatedUser.setId(existingUser.getId());
        updatedUser = userService.saveOrUpdate(updatedUser);
        return userMapper.toDto(updatedUser);
    }

    @Override
    @Transactional
    public void delete(Long id) throws NotFoundException {
        User user = userService.get(id);
        userService.delete(user);
    }
} 