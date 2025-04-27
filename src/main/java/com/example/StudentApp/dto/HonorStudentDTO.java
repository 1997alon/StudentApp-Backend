package com.example.StudentApp.dto;

import com.example.StudentApp.model.Department;

// I am using DTO right now for passing Honor Students with the 3 cols
// The cols are: email, department and gpa
// But I think dynamic approach maybe be better in this case for future changing
// nevertheless I will use DTO because is one of the cores of spring.
public class HonorStudentDTO {
    private String email;
    private Department department;
    private Integer gpa;

    public HonorStudentDTO(String email, Department department, Integer gpa) {
        this.email = email;
        this.department = department;
        this.gpa = gpa;
    }

    public String getEmail() {
        return email;
    }

    public Department getDepartment() {
        return department;
    }

    public Integer getGpa() {
        return gpa;
    }
}

