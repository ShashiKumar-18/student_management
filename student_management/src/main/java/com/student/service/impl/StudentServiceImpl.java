package com.student.service.impl;

import com.student.entity.Student;
import com.student.repository.StudentRepository;
import com.student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;
   
    public void registerStudent(Student student) {
        if (studentRepository.findByEmailId(student.getEmailId()).isPresent()) {
            throw new RuntimeException("Email already exists!");
        }
        studentRepository.save(student);
    }

    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));
    }

    @Override
    public void updateStudent(Student student) {
        studentRepository.save(student);
    }

    @Override
    public void deleteStudentById(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public Optional<Student> findByEmailAndPassword(String emailId, String password) {
        return studentRepository.findByEmailIdAndPassword(emailId, password);
    }


    @Override
    public boolean existsByEmailId(String emailId) {
        return studentRepository.findByEmailId(emailId).isPresent();
    }

    @Override
    public Optional<Student> findByEmailId(String emailId) {
        return studentRepository.findByEmailId(emailId);
    }


}
