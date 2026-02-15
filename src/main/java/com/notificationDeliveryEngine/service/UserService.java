package com.notificationDeliveryEngine.service;

import org.springframework.stereotype.Service;
import com.notificationDeliveryEngine.entity.User;
import com.notificationDeliveryEngine.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Save a new user
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Get user by username
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
