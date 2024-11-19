package com.zahid.task_management_system.exceptions;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException() {
        super("Task with this ID not found");
    }
}
