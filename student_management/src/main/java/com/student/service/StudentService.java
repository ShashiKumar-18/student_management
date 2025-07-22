package com.student.service;

import com.student.entity.Student;
import java.util.List;
import java.util.Optional;

public interface StudentService {
    Student saveStudent(Student student);
    List<Student> getAllStudents();
    Student getStudentById(Long id);
    void updateStudent(Student student);
    void deleteStudentById(Long id);
    Optional<Student> findByEmailAndPassword(String emailId, String password);
	boolean existsByEmailId(String emailId);
	void registerStudent(Student student);
	Optional<Student> findByEmailId(String emailId);
}
