package com.example.StudentApp.exception;

import com.example.StudentApp.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// Global exception handler for the entire app
// It catches specific exceptions (like StudentAppException) and others (generic exceptions)
@ControllerAdvice
public class GlobalExceptionHandler {

    // for StudentAppException
    @ExceptionHandler(StudentAppException.class)
    public ResponseEntity<Map<String, Object>> handleStudentAppException(StudentAppException ex) {
        Map<String, Object> response = ResponseUtil.buildResponse(
                ex.getAction(), false, ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
