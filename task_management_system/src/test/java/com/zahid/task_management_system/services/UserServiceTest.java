package com.zahid.task_management_system.services;

import com.zahid.task_management_system.exceptions.UserNotFoundException;
import com.zahid.task_management_system.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @Test
    public void findUserByName_NonexistentName_ThrowUserNotFoundException() {
        Mockito.when(userRepository.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.empty());
        Executable executable = () -> userService.findUserByName(Mockito.anyString());
        Assertions.assertThrows(UserNotFoundException.class, executable);
    }
}
