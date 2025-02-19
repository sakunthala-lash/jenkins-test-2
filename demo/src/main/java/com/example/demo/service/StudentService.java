package com.example.demo.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.StudentEntity;
import com.example.demo.repository.StudentRepository;

import java.util.List;

@Service
public class StudentService {


    
    @Autowired
    private StudentRepository studentRepository;

    public List<StudentEntity> getAllStudents() { //get all
        return studentRepository.findAll(); 
    }

    public StudentEntity getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null); 
    }

    public StudentEntity createStudent(StudentEntity student) {
        return studentRepository.save(student);
    }

    public StudentEntity updateStudent(Long id, StudentEntity studentDetails) {
        StudentEntity student = studentRepository.findById(id).orElseThrow();
        student.setName(studentDetails.getName());
        student.setAge(studentDetails.getAge());
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}
