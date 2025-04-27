package com.example.StudentApp.util;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {


    public static Map<String, Object> buildResponse(String action, boolean success, String message, Object data) {
        // Standardized response format for all API responses
        // response and validation response build in the same way
        Map<String, Object> response = new HashMap<>();
        response.put("action", action);
        response.put("success", success);
        response.put("message", message);
        if (data != null) {
            response.put("data", data);
        }
        return response;
    }

    public static Map<String, Object> buildResponse(String action, boolean success, String message) {
        return buildResponse(action, success, message, null);
    }
}
