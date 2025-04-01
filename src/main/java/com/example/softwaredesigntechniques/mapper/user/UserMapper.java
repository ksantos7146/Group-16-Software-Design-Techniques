package com.example.softwaredesigntechniques.mapper.user;

import com.example.softwaredesigntechniques.domain.user.User;
import com.example.softwaredesigntechniques.dto.http.user.UserDto;
import com.example.softwaredesigntechniques.dto.http.user.UserRequest;

import java.util.Collection;

public interface UserMapper {
    UserDto toDto(User user);
    User toUser(UserRequest userRequest);
    Collection<UserDto> toDto(Collection<User> users);
} 