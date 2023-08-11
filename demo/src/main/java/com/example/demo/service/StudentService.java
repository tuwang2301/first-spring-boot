package com.example.demo.service;

import com.example.demo.entities.ClassRoom;
import com.example.demo.entities.Student;
import com.example.demo.entities.Subject;
import com.example.demo.enumUsages.*;
import com.example.demo.errorhandler.StudentErrors;
import com.example.demo.errorhandler.StudentException;
import com.example.demo.repository.*;
import com.example.demo.specification.StudentSpecification;
import com.example.demo.validate.Validate;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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


    public Student addNewStudent(StudentDTO studentDTO) {
        Optional<Student> studentByEmail = studentRepository
                .findStudentByEmail(studentDTO.getEmail());
        if (studentByEmail.isPresent()) {
            throw new StudentException(StudentErrors.Email_Taken);
        }
        Validate.validateDate(studentDTO.getDob(), Student.class);
        Validate.validateGender(studentDTO.getGender());
        Validate.validateRank(studentDTO.getRank());
        Validate.validateConduct(studentDTO.getConduct());

        Student student = new Student();
        student.loadFromDto(studentDTO);
        studentRepository.save(student);
        return student;
    }

    public void deleteStudent(Long id) {
        boolean exists = studentRepository.existsById(id);
        if (!exists) {
            throw new StudentException(StudentErrors.NotFound_Student);
        }
        studentRepository.deleteById(id);
    }

    @Transactional
    public void updateStudent(Student student) {
        Optional<Student> found = studentRepository.findById(student.getId());
        if (!found.isPresent()) throw new IllegalArgumentException("Student not found");
        for (Student s : studentRepository.findAll()) {
            if (s.getId() != found.get().getId() && s.getEmail().equals(student.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
        }
        found.get().setEmail(student.getEmail());
        found.get().setName(student.getName());
        studentRepository.save(found.get());
    }

    @Transactional
    public Student updateStudent(Long studentId, StudentDTO studentDTO) {
        if (!studentRepository.existsById(studentId)) {
            throw new StudentException(StudentErrors.NotFound_Student);
        }
        Student student = studentRepository.findById(studentId).get();
        if (studentDTO.getName() != null && !studentDTO.getName().trim().isEmpty() && studentDTO.getName().length() > 0 && !Objects.equals(student.getName(), studentDTO.getName())) {
            student.setName(studentDTO.getName());
        }
        if (studentDTO.getEmail() != null && !studentDTO.getEmail().trim().isEmpty() && studentDTO.getEmail().length() > 0 && !Objects.equals(student.getEmail(), studentDTO.getEmail())) {
            student.setEmail(studentDTO.getEmail());
        }
        for (Student s : studentRepository.findAll()) {
            if (s.getId() != studentId && s.getEmail().equals(studentDTO.getEmail())) {
                throw new StudentException(StudentErrors.Email_Taken);
            }
        }
        if (studentDTO.getDob() != null && !studentDTO.getDob().trim().isEmpty()) {
            LocalDate dob = Validate.validateDate(studentDTO.getDob(), Student.class);
            student.setDob(dob);
        }
        if (studentDTO.getGender() != null && !studentDTO.getGender().trim().isEmpty()) {
            Gender gender = Validate.validateGender(studentDTO.getGender());
            student.setGender(gender);
        }
        if (studentDTO.getRank() != null && !studentDTO.getRank().trim().isEmpty()) {
            Rank rank = Validate.validateRank(studentDTO.getRank());
            student.setRank(rank);
        }
        if (studentDTO.getConduct() != null && !studentDTO.getConduct().trim().isEmpty()) {
            Conduct conduct = Validate.validateConduct(studentDTO.getConduct());
            student.setConduct(conduct);
        }

        return student;
    }

    public List<Student> getStudentsBySubjectId(Long subjectId) {

        if (!subjectRepository.existsById(subjectId)) {
            throw new IllegalArgumentException();
        }
        return (List<Student>) subjectRepository.findById(subjectId).get().getStudents();
    }

    public List<Student> getStudentsByClassRoomId(Long classRoomId) {

        if (!subjectRepository.existsById(classRoomId)) {
            throw new IllegalArgumentException();
        }
        return (List<Student>) classRoomRepository.findById(classRoomId).get().getStudents();
    }

    public Page<Student> searchStudents(String studentName, String className, String subjectName, String gender, String rank, String conduct, String classBlock, String subjectBlock, int currentPage, int pageSize) {
        Specification spec = Specification.where(null);


        if(studentName != null){
            spec = spec.and(StudentSpecification.nameContains(studentName));
        }

        if (className != null) {
            spec = spec.and(StudentSpecification.inClass(className));
        }
        if (subjectName != null) {
            spec = spec.and(StudentSpecification.joinSubject(subjectName));
        }
        if (gender != null) {
            Gender g = Validate.validateGender(gender);
            spec = spec.and(StudentSpecification.hasSex(g));
        }
        if (rank != null) {
            Rank r = Validate.validateRank(rank);
            spec = spec.and(StudentSpecification.hasRank(r));
        }
        if (conduct != null) {
            Conduct c = Validate.validateConduct(conduct);
            spec = spec.and(StudentSpecification.hasConduct(c));
        }
        if (classBlock != null) {
            Block b = Validate.validateBlock(classBlock, ClassRoom.class);
            spec = spec.and(StudentSpecification.classBlock(b));
        }
        if (subjectBlock != null) {
            Block b = Validate.validateBlock(subjectBlock, Subject.class);
            spec = spec.and(StudentSpecification.subjectBlock(b));
        }

        Page<Student> students = studentRepository.findAll(spec,PageRequest.of(currentPage-1,pageSize));

        return students;
    }

    public Student getStudentById(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            return null;
        }
        return studentRepository.findById(studentId).get();
    }

    public Student addStudentByEntity(Student student) {
        studentRepository.save(student);
        return student;
    }
}
