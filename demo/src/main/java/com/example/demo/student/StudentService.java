package com.example.demo.student;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        Optional<Student> studentByEmail = studentRepository
                .findStudentByEmail(student.getEmail());
        if(studentByEmail.isPresent()){
            throw new IllegalArgumentException("email taken");
        }
        studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        boolean exists = studentRepository.existsById(id);
        if(!exists){
            throw new IllegalArgumentException(
                    "student with id " + id + " does not exist"
            );
        }
        studentRepository.deleteById(id);
    }

    @Transactional
    public void updateStudent(Student student) {
        Optional<Student> found = studentRepository.findById(student.getId());
        if(!found.isPresent()) throw new IllegalArgumentException("Student not found");
        for (Student s: studentRepository.findAll()) {
            if(s.getId() != found.get().getId() && s.getEmail().equals(student.getEmail())){
                throw new IllegalArgumentException("Email already exists");
            }
        }
        found.get().setEmail(student.getEmail());
        found.get().setName(student.getName());
        studentRepository.save(found.get());
    }

    @Transactional
    public void updateStudent(Long studentId, String name, String email) {
        Student student = studentRepository.findById(studentId).orElseThrow(()
                -> new IllegalArgumentException("Student not found"));
        if(name != null && name.length()>0 && !Objects.equals(student.getName(), name)){
            student.setName(name);
        }
        if(email != null && email.length() > 0 && !Objects.equals(student.getEmail(), email)){
            student.setEmail(email);
        }
    }
}
