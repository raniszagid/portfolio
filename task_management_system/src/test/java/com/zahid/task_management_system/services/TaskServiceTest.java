package com.zahid.task_management_system.services;

import com.zahid.task_management_system.exception.TaskNotFoundException;
import com.zahid.task_management_system.model.Status;
import com.zahid.task_management_system.model.Task;
import com.zahid.task_management_system.repositories.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

@SpringBootTest
public class TaskServiceTest {
    @Autowired
    private TaskService taskService;
    @MockBean
    private TaskRepository taskRepository;
    @Test
    public void findOneById_NonexistentId_ThrowTaskNotFoundException() {
        Mockito.when(taskRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        Executable executable = () -> taskService.findOneById(Mockito.anyInt());
        Assertions.assertThrows(TaskNotFoundException.class, executable);
    }
}
