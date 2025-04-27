package com.example.StudentApp.service;

import com.example.StudentApp.model.Department;
import com.example.StudentApp.model.Student;
import com.example.StudentApp.repository.StudentRepository;
import com.example.StudentApp.util.RefBoolean;
import com.example.StudentApp.util.StudentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.domain.Sort;
import java.util.stream.Collectors;

@Service
public class StudentService {
    // regular route

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    @Autowired
    private StudentRepository studentRepository;
    private static final Integer EXCELLENT_GPA_THRESHOLD = 90;
    private static final List<String> VALID_SORT_FIELDS = List.of("id", "firstName", "lastName", "email", "department", "gpa");
    private static final List<String> VALID_SORT_DIRECTIONS = List.of("asc", "desc");


    @Async
    public CompletableFuture<Boolean> addStudent(Student student) {
        try {
            // check that the email is unique to add
            Optional<Student> existingStudent = studentRepository.findByEmail(student.getEmail());
            if (existingStudent.isPresent()) {
                return CompletableFuture.completedFuture(false);
            }
            studentRepository.save(student);
            return CompletableFuture.completedFuture(true);
        } catch (DataIntegrityViolationException e) {
            return CompletableFuture.completedFuture(false);
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(false);
        }
    }

    @Async
    public CompletableFuture<List<Student>> getStudents() {
        // Return the list wrapped in a CompletableFuture because
        // when using async spring allowed
        // only void or <future> type so this is future type.
        List<Student> students = studentRepository.findAll();
        return CompletableFuture.completedFuture(students);
    }

    @Async
    public CompletableFuture<List<Student>> getExcellent(String direction) {
        // check the direction that was given is valid
        if (!VALID_SORT_DIRECTIONS.contains(direction.toLowerCase())) {
            throw new IllegalArgumentException("Invalid sort direction: " + direction + ". Valid directions are 'asc' or 'desc'.");
        }
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(Sort.Order.asc("gpa")) : Sort.by(Sort.Order.desc("gpa"));
        List<Student> excellentStudents = studentRepository.findByGpaGreaterThanEqual(EXCELLENT_GPA_THRESHOLD, sort);
        return CompletableFuture.completedFuture(excellentStudents);
    }

    @Async
    public CompletableFuture<Boolean> updateStudent(Student student, long id, RefBoolean emailExist) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()) {
            Student existingStudent = optionalStudent.get();
            Optional<Student> emailOwner = studentRepository.findByEmail(student.getEmail());
            // check if the mail belong to someone else.
            if (emailOwner.isPresent() && emailOwner.get().getId() != id) {
                emailExist.set(true);
                return CompletableFuture.completedFuture(false);
            }
            existingStudent.setFirstName(student.getFirstName());
            existingStudent.setLastName(student.getLastName());
            existingStudent.setEmail(student.getEmail());
            existingStudent.setDepartment(student.getDepartment());
            existingStudent.setGpa(student.getGpa());

            studentRepository.save(existingStudent);
            return CompletableFuture.completedFuture(true);
        } else {
            return CompletableFuture.completedFuture(false);
        }
    }


    @Async
    public CompletableFuture<List<Map<String, Object>>> getDepartments() {
        Department[] departments = Department.values();
        List<Map<String, Object>> departmentList = new ArrayList<>();

        for (int i = 0; i < departments.length; i++) {
            Map<String, Object> deptMap = new HashMap<>();
            deptMap.put("index", i);
            deptMap.put("name", departments[i].name());
            logger.info("Processed department: index={}, name={}", i, departments[i].name());
            departmentList.add(deptMap);
        }
        return CompletableFuture.completedFuture(departmentList);
    }

    @Async
    public CompletableFuture<List<Student>> getSortedStudents(String sortBy, String direction) {
        if (!VALID_SORT_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        }
        if (!VALID_SORT_DIRECTIONS.contains(direction.toLowerCase())) {
            throw new IllegalArgumentException("Invalid sort direction: " + direction + ". Valid directions are 'asc' or 'desc'.");
        }
        List<Student> students;

        if ("department".equals(sortBy)) {
            students = studentRepository.findAll();
            students = StudentUtil.sortByDepartment(students, direction);
        } else {
            Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            students = studentRepository.findAll(sort);
        }
        return CompletableFuture.completedFuture(students);
    }

    @Async
    public CompletableFuture<List<Student>> getSortedExcellentStudents(String sortBy, String direction) {
        if (!VALID_SORT_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        }
        if (!VALID_SORT_DIRECTIONS.contains(direction.toLowerCase())) {
            throw new IllegalArgumentException("Invalid sort direction: " + direction + ". Valid directions are 'asc' or 'desc'.");
        }
        List<Student> students;
        if ("department".equals(sortBy)) {
            // Special sorting by ENUM (department) - manual sorting
            students = studentRepository.findByGpaGreaterThanEqual(EXCELLENT_GPA_THRESHOLD);
            students = StudentUtil.sortByDepartment(students, direction);
        } else {
            Sort sort = direction.equalsIgnoreCase("desc")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            students = studentRepository.findByGpaGreaterThanEqual(EXCELLENT_GPA_THRESHOLD, sort);
        }
        return CompletableFuture.completedFuture(students);
    }


    @Async
    public CompletableFuture<List<Student>> getFilteredStudents(String filterField, String filterValue) {
        List<String> validFilterFields = List.of("firstName", "lastName", "department");
        if (!validFilterFields.contains(filterField)) {
            throw new IllegalArgumentException("You can only filter by: firstName, lastName, or department.");
        }
        List<Student> allStudents = studentRepository.findAll();
        List<Student> filtered = allStudents.stream()
                .filter(student -> {
                    switch (filterField) {
                        case "firstName":
                            return student.getFirstName().toLowerCase().startsWith(filterValue.toLowerCase());
                        case "lastName":
                            return student.getLastName().toLowerCase().startsWith(filterValue.toLowerCase());
                        case "department":
                            try {
                                Department departmentEnum = Department.valueOf(filterValue.toUpperCase());
                                return student.getDepartment().equals(departmentEnum);
                            } catch (IllegalArgumentException e) {
                                throw new IllegalArgumentException("Invalid department: " + filterValue);
                            }
                        default:
                            return false;
                    }
                })
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(filtered);
    }


    @Async
    public CompletableFuture<List<Student>> getFilteredExcellentStudents(String filterField, String filterValue) {
        List<String> validFilterFields = List.of("firstName", "lastName", "department");
        if (!validFilterFields.contains(filterField)) {
            throw new IllegalArgumentException("You can only filter by: firstName, lastName, or department.");
        }
        // Get excellent students sorted by GPA in descending order
        List<Student> excellentStudents = studentRepository.findByGpaGreaterThanEqual(
                EXCELLENT_GPA_THRESHOLD,
                Sort.by(Sort.Direction.DESC, "gpa")
        );
        // Filter based on prefix or department
        List<Student> filtered = excellentStudents.stream()
                .filter(student -> {
                    switch (filterField) {
                        case "firstName":
                            return student.getFirstName().toLowerCase().startsWith(filterValue.toLowerCase());
                        case "lastName":
                            return student.getLastName().toLowerCase().startsWith(filterValue.toLowerCase());
                        case "department":
                            try {
                                Department departmentEnum = Department.valueOf(filterValue.toUpperCase());
                                return student.getDepartment().equals(departmentEnum);
                            } catch (IllegalArgumentException e) {
                                throw new IllegalArgumentException("Invalid department: " + filterValue);
                            }
                        default:
                            return false;
                    }
                })
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(filtered);
    }


}
