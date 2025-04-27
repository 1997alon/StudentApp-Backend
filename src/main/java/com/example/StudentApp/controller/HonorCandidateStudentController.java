package com.example.StudentApp.controller;

import com.example.StudentApp.dto.HonorStudentDTO;
import com.example.StudentApp.exception.StudentAppException;
import com.example.StudentApp.service.HonorCandidateStudentService;
import com.example.StudentApp.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/honorStudent")
public class HonorCandidateStudentController {

    @Autowired
    private HonorCandidateStudentService studentService;
    private static final List<String> FIELDS = List.of("gpa", "department", "email");

    @GetMapping("/getHonorStudents")
    public ResponseEntity<Map<String, Object>> getHonorStudents(
            @RequestParam(defaultValue = "desc") String direction) {
        String action = "getHonorStudents";
        try {
            List<HonorStudentDTO> honorCandidates = studentService.getHonorCandidates(direction).get();
            return ResponseEntity.ok(
                    ResponseUtil.buildResponse(action, true, "Honor students fetched successfully.", honorCandidates)
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    ResponseUtil.buildResponse(action, false, "Error: " + e.getMessage())
            );
        }
    }

    @GetMapping("/getHighestGpaPerDepartment")
    public ResponseEntity<Map<String, Object>> getHighestGpaPerDepartment() {
        String action = "getHighestGpaPerDepartment";
        try {
            List<HonorStudentDTO> highestGpaPerDepartment = studentService.getHighestGpaPerDepartment().get();
            return ResponseEntity.ok(
                    ResponseUtil.buildResponse(action, true, "Highest GPA for department fetched successfully.", highestGpaPerDepartment)
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new StudentAppException(action, e.getMessage());
        }
    }

    @GetMapping("/sortHonorStudents")
    public ResponseEntity<Map<String, Object>> sortHonorStudents(
            @RequestParam(defaultValue = "gpa") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        // can sort the list of honor stunent by (gpa, department and email)
        String action = "sortHonorStudents";
        try {
            List<HonorStudentDTO> sortedStudents = studentService.getSortedHonorStudents(sortBy, direction).get();
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
}
