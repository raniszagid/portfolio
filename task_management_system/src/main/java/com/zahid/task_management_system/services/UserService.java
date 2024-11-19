package com.zahid.task_management_system.services;

import com.zahid.task_management_system.models.User;
import com.zahid.task_management_system.repositories.UserRepository;
import com.zahid.task_management_system.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserByName(String name) {
        return userRepository.findByUsername(name)
                .orElseThrow(UserNotFoundException::new);
    }
}