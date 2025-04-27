package com.example.StudentApp.util;

import com.example.StudentApp.exception.StudentAppException;
import com.example.StudentApp.model.Student;

import java.util.*;
import java.util.stream.Collectors;

public class StudentUtil {

    public static List<Student> sortByDepartment(List<Student> students, String direction) {
        // department is ENUM so need to sorting by the value and not index
        if (!"asc".equalsIgnoreCase(direction) && !"desc".equalsIgnoreCase(direction)) {
            throw new IllegalArgumentException("Invalid sort direction: " + direction + ". Valid directions are 'asc' or 'desc'.");
        }
        Comparator<Student> comparator = Comparator.comparing(s -> s.getDepartment().name());
        if ("desc".equalsIgnoreCase(direction)) {
            comparator = comparator.reversed();
        }
        students.sort(comparator);
        return students;
    }
}
