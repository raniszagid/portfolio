package com.zahid.task_management_system.exception;

public class InaccessibleActionException extends RuntimeException {
    public InaccessibleActionException() {
        super("This action is inaccessible for current user");
    }
}
