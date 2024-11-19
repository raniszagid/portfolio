package com.zahid.task_management_system.services;

import com.zahid.task_management_system.models.User;
import com.zahid.task_management_system.repositories.UserRepository;
import com.zahid.task_management_system.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public TaskUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(s);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new PersonDetails(user.get());
    }

    public boolean check(String s) {
        Optional<User> user = userRepository.findByUsername(s);
        return user.isEmpty();
    }
}