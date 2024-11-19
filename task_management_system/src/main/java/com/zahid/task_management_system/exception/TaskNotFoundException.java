package com.zahid.task_management_system.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException() {
        super("Task with this ID not found");
    }
}
