package com.example.softwaredesigntechniques.mapper.user;

import com.example.softwaredesigntechniques.domain.user.User;
import com.example.softwaredesigntechniques.dto.user.UserDto;
import com.example.softwaredesigntechniques.dto.user.UserRequest;

import java.util.Collection;

public interface UserMapper {
    UserDto toDto(User user);
    User toUser(UserRequest userRequest);
    User toEntity(UserRequest userRequest);
    void updateEntity(User user, UserRequest userRequest);
    Collection<UserDto> toDto(Collection<User> users);
} 