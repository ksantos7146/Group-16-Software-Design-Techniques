package com.example.softwaredesigntechniques.service.user.impl;

import com.example.softwaredesigntechniques.domain.user.User;
import com.example.softwaredesigntechniques.exception.NotFoundException;
import com.example.softwaredesigntechniques.repository.user.UserRepository;
import com.example.softwaredesigntechniques.service.common.impl.DefaultBaseService;
import com.example.softwaredesigntechniques.service.user.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DefaultUserService extends DefaultBaseService<User, Long> implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DefaultUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        super(userRepository);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsernameAndDeletedByIsNull(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailAndDeletedByIsNull(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findByFirstName(String firstName) {
        return userRepository.findByFirstNameAndDeletedByIsNull(firstName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findByLastName(String lastName) {
        return userRepository.findByLastNameAndDeletedByIsNull(lastName);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsernameAndDeletedByIsNull(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmailAndDeletedByIsNull(email);
    }

    @Override
    @Transactional
    public User saveOrUpdate(User user) {
        // Check if this is a new user or password is being updated
        if (user.getId() == null || user.getPassword() != null && !user.getPassword().isEmpty()) {
            // Encode the password before saving
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else if (user.getId() != null) {
            // For existing users, keep the current password if not provided
            userRepository.findById(user.getId()).ifPresent(existingUser -> {
                if (user.getPassword() == null || user.getPassword().isEmpty()) {
                    user.setPassword(existingUser.getPassword());
                }
            });
        }
        return super.saveOrUpdate(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User login(String username, String password) throws NotFoundException {
        User user = findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found with username: " + username));
        System.out.println("Raw Password: " + password);
        System.out.println("Encoded Password: " + user.getPassword());
        System.out.println("Matches: " + passwordEncoder.matches(password, user.getPassword()));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new NotFoundException("Invalid password");
        }

        return user;
    }
} 