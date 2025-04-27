package com.example.StudentApp.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

// ENUM for department as in DB
public enum Department {
    COMPUTER_SCIENCE,
    ENGINEER,
    MATHEMATICS,
    PHYSICS;

    @JsonCreator
    public static Department fromString(String value) {
        try {
            return Department.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid department: " + value);
        }
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}

