package com.example.demo.service;

import com.example.demo.entities.Student;
import com.example.demo.entities.Subject;
import com.example.demo.enumUsages.Block;
import com.example.demo.enumUsages.ClassErrors;
import com.example.demo.enumUsages.SubjectErrors;
import com.example.demo.repository.StudentDTO;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.SubjectDTO;
import com.example.demo.repository.SubjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;

    public List<Subject> getSubjects() {
        return subjectRepository.findAll();
    }

    public SubjectErrors addSubject(SubjectDTO subjectDTO) {
        if (subjectRepository.findByName(subjectDTO.getName()).isPresent()) {
            return SubjectErrors.Name_Exist;
        }
        try {
            Integer credits = Integer.parseInt(subjectDTO.getCredits());
        } catch (NumberFormatException e) {
            return SubjectErrors.Credits_Invalid;
        }
        try {
            LocalDate startTime = LocalDate.parse(subjectDTO.getStartTime());
            if (LocalDate.now().isAfter(startTime)) {
                return SubjectErrors.StartTime_Invalid;
            }
            LocalDate endTime = LocalDate.parse(subjectDTO.getEndTime());
            if (Period.between(startTime, endTime).getMonths() < 1) {
                return SubjectErrors.EndTime_Invalid;
            }
        } catch (Exception e) {
            return SubjectErrors.Date_Invalid;
        }
        try{
            Block block1 = Block.valueOf(subjectDTO.getClassBlock());
        }catch (Exception e){
            return SubjectErrors.Block_Invalid;
        }

        return null;
    }

    @Transactional
    public SubjectErrors updateSubject(Long subjectId, SubjectDTO subjectDTO) {
        Optional<Subject> found = subjectRepository.findById(subjectId);
        if (!found.isPresent()) {
            return SubjectErrors.NotFound_Subject;
        }
        if (LocalDate.now().isAfter(found.get().getStartTime())) {
            return SubjectErrors.Started_Subject;
        }
        for (Subject s : subjectRepository.findAll()) {
            if (s.getId() != subjectId && s.getName().equalsIgnoreCase(subjectDTO.getName())) {
                return SubjectErrors.Name_Exist;
            }
        }
        if (subjectDTO.getName() != null && !subjectDTO.getName().trim().isEmpty() && subjectDTO.getName().length() > 0 && !Objects.equals(found.get().getName(), subjectDTO.getName())) {
            found.get().setName(subjectDTO.getName());
        }
        if (subjectDTO.getCredits() != null && !subjectDTO.getCredits().trim().isEmpty()) {
            try {
                Integer credits = Integer.parseInt(subjectDTO.getCredits());
                found.get().setCredits(credits);
            } catch (NumberFormatException e) {
                return SubjectErrors.Credits_Invalid;
            }
        }

        if (subjectDTO.getStartTime() != null && !subjectDTO.getStartTime().trim().isEmpty()) {
            try {
                LocalDate startTime = LocalDate.parse(subjectDTO.getStartTime());
                if (LocalDate.now().isAfter(startTime)) {
                    return SubjectErrors.StartTime_Invalid;
                }
                found.get().setStartTime(startTime);
            } catch (Exception e) {
                return SubjectErrors.Date_Invalid;
            }
        }
        if (subjectDTO.getEndTime() != null && !subjectDTO.getEndTime().trim().isEmpty()) {
            try {
                LocalDate endTime = LocalDate.parse(subjectDTO.getEndTime());
                if (Period.between(found.get().getStartTime(), endTime).getMonths() < 1) {
                    return SubjectErrors.EndTime_Invalid;
                }
                found.get().setEndTime(endTime);
            } catch (Exception e) {
                return SubjectErrors.Date_Invalid;
            }
        }
        try{
            Block block1 = Block.valueOf(subjectDTO.getClassBlock());
        }catch (Exception e){
            return SubjectErrors.Block_Invalid;
        }

        return null;
    }


    public void deleteSubject(Long subjectId) {
        Optional<Subject> found = subjectRepository.findById(subjectId);
        if (!found.isPresent()) {
            throw new IllegalStateException();
        }
        subjectRepository.deleteById(subjectId);
    }

    @Transactional
    public SubjectErrors registerSubjects(Long studentId, Long subjectId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (!student.isPresent()) {
            return SubjectErrors.NotFound_Student;
        }
        Optional<Subject> subject = subjectRepository.findById(subjectId);
        if (!subject.isPresent()) {
            return SubjectErrors.NotFound_Subject;
        }
        Subject s = subject.get();
        if (student.get().getSubjects().contains(s)) {
            return SubjectErrors.Already_Register;
        }
        if (LocalDate.now().isAfter(subject.get().getEndTime())) {
            return SubjectErrors.Over_Subject;
        }
        if (Period.between(subject.get().getStartTime(), LocalDate.now()).getMonths() >= 1) {
            return SubjectErrors.Late_Register;
        }
        s.getStudents().add(student.get());
        student.get().getSubjects().add(subject.get());
        return null;
    }

    public List<Subject> getSubjectsByStudentId(Long studentId) {
        return studentRepository.findById(studentId).get().getSubjects().stream().toList();
    }

    @Transactional
    public SubjectErrors unregisterSubject(Long studentId, Long subjectId) {
        if (!studentRepository.existsById(studentId)) {
            return SubjectErrors.NotFound_Student;
        }
        Student student = studentRepository.findById(studentId).get();

        if (!subjectRepository.existsById(subjectId)) {
            return SubjectErrors.NotFound_Subject;
        }
        Subject subject = subjectRepository.findById(subjectId).get();

        if (!student.getSubjects().contains(subject)) {
            return SubjectErrors.No_Register;
        }

        if (LocalDate.now().isAfter(subject.getStartTime())) {
            return SubjectErrors.Started_Subject;
        }

        student.getSubjects().remove(subject);
        subject.getStudents().remove(student);

        return null;
    }
}
