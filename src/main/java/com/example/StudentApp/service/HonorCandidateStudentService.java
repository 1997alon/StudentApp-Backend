package com.example.StudentApp.service;

import com.example.StudentApp.dto.HonorStudentDTO;
import com.example.StudentApp.exception.StudentAppException;
import com.example.StudentApp.model.Department;
import com.example.StudentApp.model.Student;
import com.example.StudentApp.repository.StudentRepository;
import com.example.StudentApp.util.StudentUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class HonorCandidateStudentService {

    private static final Integer EXCELLENT_GPA_THRESHOLD  = 90;

    @Autowired
    private StudentRepository studentRepository;

    @Async
    public CompletableFuture<List<HonorStudentDTO>> getHonorCandidates(String direction) {
        List<String> validDirections = List.of("asc", "desc");
        if (!validDirections.contains(direction.toLowerCase())) {
            throw new IllegalArgumentException("Invalid sort direction: " + direction + ". Valid directions are 'asc' or 'desc'.");
        }

        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by("gpa").ascending() : Sort.by("gpa").descending();
        List<Student> honorCandidates = studentRepository.findByGpaGreaterThanEqual(EXCELLENT_GPA_THRESHOLD, sort);

        List<HonorStudentDTO> result = honorCandidates.stream()
                .map(student -> new HonorStudentDTO(student.getEmail(), student.getDepartment(), student.getGpa()))
                .collect(Collectors.toList());

        return CompletableFuture.completedFuture(result);
    }

    @Async
    public CompletableFuture<List<HonorStudentDTO>> getHighestGpaPerDepartment() {
        try {
            List<Student> topStudents = studentRepository.findTopStudentInEachDepartment();

            List<HonorStudentDTO> result = topStudents.stream()
                    .map(student -> new HonorStudentDTO(student.getEmail(), student.getDepartment(), student.getGpa()))
                    .collect(Collectors.toList());

            return CompletableFuture.completedFuture(result);
        } catch (Exception e) {
            throw new StudentAppException("getTopStudentInEachDepartment", e.getMessage());
        }
    }

    @Async
    public CompletableFuture<List<HonorStudentDTO>> getSortedHonorStudents(String sortBy, String direction) {
        List<String> validFields = List.of("email", "department", "gpa");
        List<String> validDirections = List.of("asc", "desc");

        if (!validFields.contains(sortBy.toLowerCase())) {
            throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        }
        if (!validDirections.contains(direction.toLowerCase())) {
            throw new IllegalArgumentException("Invalid sort direction: " + direction + ". Valid directions are 'asc' or 'desc'.");
        }

        List<Student> sortedStudents;
        if ("department".equalsIgnoreCase(sortBy)) {
            sortedStudents = studentRepository.findByGpaGreaterThanEqual(EXCELLENT_GPA_THRESHOLD);
            sortedStudents = StudentUtil.sortByDepartment(sortedStudents, direction);
        } else {
            Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            sortedStudents = studentRepository.findByGpaGreaterThanEqual(EXCELLENT_GPA_THRESHOLD, sort);
        }

        List<HonorStudentDTO> result = sortedStudents.stream()
                .map(student -> new HonorStudentDTO(student.getEmail(), student.getDepartment(), student.getGpa()))
                .collect(Collectors.toList());

        return CompletableFuture.completedFuture(result);
    }

}
