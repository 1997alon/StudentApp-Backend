package com.example.StudentApp.controller;

import com.example.StudentApp.exception.StudentAppException;
import com.example.StudentApp.model.Student;
import com.example.StudentApp.service.StudentService;
import com.example.StudentApp.util.RefBoolean;
import com.example.StudentApp.util.ResponseUtil;
import com.example.StudentApp.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/student")
public class StudentController {
    // regular route controller - clean code and separating with honor route

    @Autowired
    private StudentService studentService;

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addStudent(
            @Valid @RequestBody Student student,
            BindingResult bindingResult) {
        // for make sure the inputs are valid
        if (bindingResult.hasErrors()) {
            return ResponseEntity.ok().body(
                    ValidationUtil.buildValidationErrorResponse(bindingResult, "addStudent")
            );
        }
        String action = "addStudent";
        try {
            boolean added = studentService.addStudent(student).get();
            if (added) {
                return ResponseEntity.ok(
                        ResponseUtil.buildResponse(action, true, "Student added successfully.")
                );
            } else {
                return ResponseEntity.ok(
                        ResponseUtil.buildResponse(action, false, "Error: Student with this email already exists.")
                );
            }
        } catch (Exception e) {
            throw new StudentAppException(action, e.getMessage());
        }
    }

    @GetMapping("/getDepartments")
    public ResponseEntity<Map<String, Object>> getDepartments() {
        // export the department options
        // because it is only one method for department I am putting it in studentController
        // In general, I suppose to add new departmentController but for one method I am leaving
        // it here.
        String action = "getDepartments";
        try {
            List<Map<String, Object>> departmentList = studentService.getDepartments().get();
            return ResponseEntity.ok(
                    ResponseUtil.buildResponse(action, true, "Departments fetched successfully.", departmentList)
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    ResponseUtil.buildResponse(action, false, "Error: " + e.getMessage())
            );
        }
    }


    @GetMapping("/getStudents")
    public ResponseEntity<Map<String, Object>> getStudents() {
        String action = "getStudents";
        try {
            List<Student> students = studentService.getStudents().get();
            return ResponseEntity.ok(
                    ResponseUtil.buildResponse(action, true, "Students fetched successfully.", students)
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    ResponseUtil.buildResponse(action, false, "Error: " + e.getMessage())
            );
        }
    }

    @GetMapping("/getExcellent")
    public ResponseEntity<Map<String, Object>> getExcellent(@RequestParam(defaultValue = "desc") String direction) {
        String action = "getExcellent";
        try {
            List<Student> students = studentService.getExcellent(direction).get();
            return ResponseEntity.ok(
                    ResponseUtil.buildResponse(action, true, "Students fetched successfully.", students)
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    ResponseUtil.buildResponse(action, false, "Error: " + e.getMessage())
            );
        }
    }


    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> updateStudent(
            @Valid @RequestBody Student student,
            BindingResult bindingResult) {
        // Flag to check if the email is the problem.
        RefBoolean emailExist = new RefBoolean(false);

        // Ensure the inputs are valid
        if (bindingResult.hasErrors()) {
            return ResponseEntity.ok().body(
                    ValidationUtil.buildValidationErrorResponse(bindingResult, "updateStudent")
            );
        }

        String action = "updateStudent";
        try {
            // Use the `id` from the Student object in the body
            boolean updated = studentService.updateStudent(student, student.getId(), emailExist).get();

            if (updated) {
                return ResponseEntity.ok(
                        ResponseUtil.buildResponse(action, true, "Student updated successfully.")
                );
            } else {
                if (emailExist.get()) {
                    return ResponseEntity.ok(
                            ResponseUtil.buildResponse(action, false, "The email you want to change already exists. Please try a different one.")
                    );
                }
                return ResponseEntity.ok(
                        ResponseUtil.buildResponse(action, false, "Student with ID " + student.getId() + " not found.")
                );
            }
        } catch (Exception e) {
            throw new StudentAppException(action, e.getMessage());
        }
    }



    @GetMapping("/sort")
    public ResponseEntity<Map<String, Object>> sortStudents(
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        String action = "sort";
        try {
            List<Student> sortedStudents = studentService.getSortedStudents(sortBy, direction).get();
            return ResponseEntity.ok(
                    ResponseUtil.buildResponse(action, true, "Students sorted successfully.", sortedStudents)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok().body(
                    ResponseUtil.buildResponse(action, false, e.getMessage())
            );
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.internalServerError().body(
                    ResponseUtil.buildResponse(action, false, "Error: " + e.getMessage())
            );
        }
    }

    @GetMapping("/sortExcellent")
    public ResponseEntity<Map<String, Object>> sortExcellentStudents(
            @RequestParam(defaultValue = "gpa") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        String action = "sortExcellent";
        try {
            List<Student> sortedStudents = studentService.getSortedExcellentStudents(sortBy, direction).get();
            return ResponseEntity.ok(
                    ResponseUtil.buildResponse(action, true, "Students sorted successfully.", sortedStudents)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok().body(
                    ResponseUtil.buildResponse(action, false, e.getMessage())
            );
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.internalServerError().body(
                    ResponseUtil.buildResponse(action, false, "Error: " + e.getMessage())
            );
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<Map<String, Object>> filterStudents(
            @RequestParam(required = false) String filterField,
            @RequestParam(required = false) String filterValue) {

        String action = "filterStudents";
        try {
            if (filterField == null || filterField.isEmpty() || filterValue == null || filterValue.isEmpty()) {
                throw new StudentAppException(action, "filterField and filterValue are required.");
            }
            List<Student> filteredStudents = studentService.getFilteredStudents(filterField, filterValue).get();
            return ResponseEntity.ok(
                    ResponseUtil.buildResponse(action, true, "Students filtered successfully.", filteredStudents)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok().body(
                    ResponseUtil.buildResponse(action, false, e.getMessage())
            );
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.internalServerError().body(
                    ResponseUtil.buildResponse(action, false, "Error: " + e.getMessage())
            );
        }
    }

    @GetMapping("/filterExcellent")
    public ResponseEntity<Map<String, Object>> filterExcellentStudents(
            @RequestParam(required = false) String filterField,
            @RequestParam(required = false) String filterValue) {

        String action = "filterStudents";
        try {
            if (filterField == null || filterField.isEmpty() || filterValue == null || filterValue.isEmpty()) {
                throw new StudentAppException(action, "filterField and filterValue are required.");
            }
            List<Student> filteredStudents = studentService.getFilteredExcellentStudents(filterField, filterValue).get();
            return ResponseEntity.ok(
                    ResponseUtil.buildResponse(action, true, "Students filtered successfully.", filteredStudents)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok().body(
                    ResponseUtil.buildResponse(action, false, e.getMessage())
            );
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.internalServerError().body(
                    ResponseUtil.buildResponse(action, false, "Error: " + e.getMessage())
            );
        }
    }

}
