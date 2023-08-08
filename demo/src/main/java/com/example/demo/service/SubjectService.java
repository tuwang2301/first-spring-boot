package com.example.demo.service;

import com.example.demo.entities.Student;
import com.example.demo.entities.Subject;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.SubjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    public List<Subject> getSubjects() {
        return  subjectRepository.findAll();
    }

    public void addSubject(Subject subject) {
        Optional<Subject> found = subjectRepository.findByName(subject.getName());
        if(found.isPresent()){
            throw new IllegalStateException();
        }
        subjectRepository.save(subject);
    }
    @Transactional
    public int updateSubject(Long subjectId, String name, Integer credits) {
        Optional<Subject> found = subjectRepository.findById(subjectId);
        if(!found.isPresent()){
            return 1;
        }
        for (Subject s: subjectRepository.findAll()) {
            if(s.getId() != subjectId && s.getName().equalsIgnoreCase(name)){
                return 2;
            }
        }
        if(name != null && name.length() > 0 && !Objects.equals(found.get().getName(),name)){
            found.get().setName(name);
        }
        if(credits != null ){
            found.get().setCredits(credits);
        }
        return 0;
    }


    public void deleteSubject(Long subjectId) {
        Optional<Subject> found = subjectRepository.findById(subjectId);
        if(!found.isPresent()){
            throw new IllegalStateException();
        }
        subjectRepository.deleteById(subjectId);
    }

    @Transactional
    public int registerSubjects(Long studentId, List<Long> subjectIds) {
        Optional<Student> student = studentRepository.findById(studentId);
        if(!student.isPresent()){
            return 1;
        }
        for (Long id: subjectIds) {
            Optional<Subject> subject = subjectRepository.findById(id);
            if(!subject.isPresent()){
                return 2;
            }
            Subject s = subject.get();
            if(s.getStudents().contains(student.get())){
                return 3;
            }
            s.getStudents().add(student.get());
        }
        return 0;
    }

    public List<Subject> getSubjectsByStudentId(Long studentId) {
        return studentRepository.findById(studentId).get().getSubjects().stream().toList();
    }

    @Transactional
    public int unregisterSubject(Long studentId, Long subjectId) {
        if(!studentRepository.existsById(studentId)){
            return 1;
        }
        Student student = studentRepository.findById(studentId).get();

        if(!subjectRepository.existsById(subjectId)){
            return 2;
        }
        Subject subject = subjectRepository.findById(subjectId).get();

        if(!student.getSubjects().contains(subject)){
            return 3;
        }

        student.getSubjects().remove(subject);
        student.setSubjects(student.getSubjects());
        subject.getStudents().remove(student);
        subject.setStudents(subject.getStudents());

        return 0;
    }
}
