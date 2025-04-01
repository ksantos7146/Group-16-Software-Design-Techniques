package com.example.softwaredesigntechniques.mapper.user.impl;

import com.example.softwaredesigntechniques.domain.user.User;
import com.example.softwaredesigntechniques.dto.http.user.UserDto;
import com.example.softwaredesigntechniques.dto.http.user.UserRequest;
import com.example.softwaredesigntechniques.mapper.user.UserMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DefaultUserMapper implements UserMapper {

    @Override
    @Transactional(readOnly = true)
    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public User toUser(UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<UserDto> toDto(Collection<User> users) {
        Collection<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            userDtos.add(toDto(user));
        }
        return userDtos;
    }
} 