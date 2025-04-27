package com.example.StudentApp.exception;

// Custom exception class that includes an "action" field
// to provide more context like which operation failed.
// use it for department ENUM
public class StudentAppException extends RuntimeException {
    private final String action;

    public StudentAppException(String action, String message) {
        super(message);
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
