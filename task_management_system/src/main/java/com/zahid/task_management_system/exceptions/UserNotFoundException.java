package com.zahid.task_management_system.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User with this email not found");
    }
}
