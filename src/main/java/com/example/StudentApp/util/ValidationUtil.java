package com.example.StudentApp.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import java.util.HashMap;
import java.util.Map;

public class ValidationUtil {

    // for errors in requests like blank, or invalid email in Student
    public static Map<String, Object> buildValidationErrorResponse(BindingResult bindingResult, String actionName) {
        // Standardized response format for all API responses as error
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("action", actionName);

        StringBuilder message = new StringBuilder("Validation errors: ");
        for (FieldError error : bindingResult.getFieldErrors()) {
            message.append(error.getField())
                    .append(" - ")
                    .append(error.getDefaultMessage())
                    .append("; ");
        }
        response.put("message", message.toString().trim());
        return response;
    }
}
