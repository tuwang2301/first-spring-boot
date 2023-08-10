package com.example.demo.service;

import com.example.demo.entities.Student;
import com.example.demo.enumUsages.*;
import com.example.demo.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private ClassRoomRepository classRoomRepository;
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public StudentErrors addNewStudent(StudentDTO studentDTO) {
        Optional<Student> studentByEmail = studentRepository
                .findStudentByEmail(studentDTO.getEmail());
        if(studentByEmail.isPresent()){
            return StudentErrors.Email_Taken;
        }
        try{
            LocalDate dob = LocalDate.parse(studentDTO.getDob());
        }catch (DateTimeParseException e){
            return StudentErrors.Date_Invalid;
        }
        try{
            Gender gender = Gender.valueOf(studentDTO.getGender());
        }catch (Exception e){
            return StudentErrors.Gender_Invalid;
        }
        try{
            Rank rank = Rank.valueOf(studentDTO.getRank());
        }catch (Exception e){
            return StudentErrors.Rank_Invalid;
        }
        try{
            Conduct conduct = Conduct.valueOf(studentDTO.getConduct());
        }catch (Exception e){
            return StudentErrors.Conduct_Invalid;
        }
        Student student = new Student();
        student.loadFromDto(studentDTO);
        studentRepository.save(student);
        return null;
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
    public StudentErrors updateStudent(Long studentId, StudentDTO studentDTO) {
        if(!studentRepository.existsById(studentId)){
            return StudentErrors.NotFound_Student;
        }
        Student student = studentRepository.findById(studentId).get();
        if(studentDTO.getName() != null && !studentDTO.getName().trim().isEmpty() && studentDTO.getName().length()>0 && !Objects.equals(student.getName(), studentDTO.getName())){
            student.setName(studentDTO.getName());
        }
        if(studentDTO.getEmail() != null && !studentDTO.getEmail().trim().isEmpty()  && studentDTO.getEmail().length() > 0 && !Objects.equals(student.getEmail(), studentDTO.getEmail())){
            student.setEmail(studentDTO.getEmail());
        }
        for (Student s: studentRepository.findAll()) {
            if(s.getId() != studentId && s.getEmail().equals(studentDTO.getEmail())){
                return StudentErrors.Email_Taken;
            }
        }
        if(studentDTO.getDob() != null && !studentDTO.getDob().trim().isEmpty()){
            try {
                LocalDate dob = LocalDate.parse(studentDTO.getDob());
                student.setDob(dob);
            }catch (Exception e){
                return StudentErrors.Date_Invalid;
            }
        }
        if(studentDTO.getGender() != null && !studentDTO.getGender().trim().isEmpty()){
            try {
                Gender gender = Gender.valueOf(studentDTO.getGender());
                student.setGender(gender);
            }catch (Exception e){
                return StudentErrors.Gender_Invalid;
            }
        }
        if(studentDTO.getRank() != null && !studentDTO.getRank().trim().isEmpty()){
            try {
                Rank rank = Rank.valueOf(studentDTO.getRank());
                student.setRank(rank);
            }catch (Exception e){
                return StudentErrors.Rank_Invalid;
            }
        }
        if(studentDTO.getConduct() != null && !studentDTO.getConduct().trim().isEmpty()){
            try {
                Conduct conduct = Conduct.valueOf(studentDTO.getConduct());
                student.setConduct(conduct);
            }catch (Exception e){
                return StudentErrors.Conduct_Invalid;
            }
        }

        return null;
    }

    public List<Student> getStudentsBySubjectId(Long subjectId) {

        if(!subjectRepository.existsById(subjectId)){
            throw new IllegalArgumentException();
        }
        return (List<Student>) subjectRepository.findById(subjectId).get().getStudents();
    }

    public List<Student> getStudentsByClassRoomId(Long classRoomId) {

        if(!subjectRepository.existsById(classRoomId)){
            throw new IllegalArgumentException();
        }
        return (List<Student>) classRoomRepository.findById(classRoomId).get().getStudents();
    }

    public List<Student> searchStudents(String className, String subjectName, Gender gender, Rank rank, Conduct conduct, Block classBlock, Block subjectBlock) {
        Specification spec = Specification.where(null);


        if (className != null) {
            spec = spec.and( StudentSpecification.inClass(className));
        }
        if (subjectName != null) {
            spec = spec.and( StudentSpecification.joinSubject(subjectName));
        }
        if (gender != null) {
            spec = spec.and(StudentSpecification.hasSex(gender));
        }
        if (rank != null) {
            spec = spec.and(StudentSpecification.hasRank(rank));
        }
        if (conduct != null) {
            spec = spec.and(StudentSpecification.hasConduct(conduct));
        }
        if (classBlock != null) {
            spec = spec.and(StudentSpecification.classBlock(classBlock));
        }
        if (subjectBlock != null) {
            spec = spec.and(StudentSpecification.subjectBlock(subjectBlock));
        }

        List<Student> students = studentRepository.findAll(spec);
        return students;
    }

    public Student getStudentById(Long studentId) {
        if(!studentRepository.existsById(studentId)){
            return null;
        }
        return studentRepository.findById(studentId).get();
    }
}
