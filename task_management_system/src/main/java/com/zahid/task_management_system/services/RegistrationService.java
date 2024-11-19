package com.zahid.task_management_system.services;

import com.zahid.task_management_system.model.User;
import com.zahid.task_management_system.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        /* для удобства ознакомления с программой:
        первый зарегистрированный пользователь - админ,
        остальные - обычные пользователи */
        if (userRepository.count() == 0)
            user.setRole("ROLE_ADMIN");
        else
            user.setRole("ROLE_USER");
        userRepository.save(user);
    }
}