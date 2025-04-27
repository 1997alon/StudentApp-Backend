package com.example.StudentApp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "student")
public class Student {
    // student class as is in DB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name cannot be blank")
    @Length(min = 1, max = 50, message = "First name must be between 2 and 50 characters")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Length(min = 1, max = 50, message = "Last name must be between 2 and 50 characters")
    @Column(nullable = false)
    private String lastName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be blank")
    @Length(max = 100, message = "Email must be at most 100 characters")
    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING) // Store enum as string in DB
    @NotNull(message = "Department cannot be null")
    @Column(nullable = false)
    private Department department;

    @Min(value = 0, message = "GPA must be at least 0")
    @Max(value = 100, message = "GPA must be at most 100")
    @NotNull(message = "GPA cannot be null")
    @Column(nullable = false)
    private Integer gpa;


    // Getters
    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setGpa(Integer gpa) {
        this.gpa = gpa;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", gpa=" + gpa +
                '}';
    }
}
