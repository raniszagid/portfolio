package com.zahid.task_management_system.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User with this email not found");
    }
}
