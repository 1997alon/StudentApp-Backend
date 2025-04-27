package com.example.StudentApp.repository;

import com.example.StudentApp.model.Department;
import com.example.StudentApp.model.Student;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    // the repository for student - for honor and regular routes

    Optional<Student> findByEmail(String email); // for adding student
    List<Student> findByGpaGreaterThanEqual(Integer gpa, Sort sort); // excellent students
    List<Student> findByGpaGreaterThanEqual(Integer gpa); // for departments sorting (special because it is ENUM)
    List<Student> findByDepartmentAndGpaGreaterThanEqual(Department department, Integer gpa, Sort sort); // excellent students from one department
    // taking the highest gpa student from each department (if there is more than one so it will give more)
    @Query("SELECT s FROM Student s WHERE s.gpa = (SELECT MAX(sub.gpa) FROM Student sub WHERE sub.department = s.department)")
    List<Student> findTopStudentInEachDepartment();
}