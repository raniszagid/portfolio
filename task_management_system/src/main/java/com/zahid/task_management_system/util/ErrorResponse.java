package com.zahid.task_management_system.util;

public class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
