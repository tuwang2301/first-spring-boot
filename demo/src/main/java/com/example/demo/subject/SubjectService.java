package com.example.demo.subject;

import com.example.demo.student.Student;
import com.example.demo.enumUsages.Block;
import com.example.demo.student.StudentErrors;
import com.example.demo.student.StudentException;
import com.example.demo.student.StudentRepository;
import com.example.demo.student.StudentSpecification;
import com.example.demo.validate.Validate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
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

    public Subject addSubject(SubjectDTO subjectDTO) {
        if (subjectRepository.findByName(subjectDTO.getName()).isPresent()) {
            throw new SubjectException(SubjectErrors.Name_Exist);
        }
        Validate.validateCredits(subjectDTO.getCredits());
        Validate.validateDate(subjectDTO.getStartTime(), Subject.class);
        LocalDate startTime = LocalDate.parse(subjectDTO.getStartTime());
        if (LocalDate.now().isAfter(startTime)) {
            throw new SubjectException(SubjectErrors.StartTime_Invalid);
        }
        Validate.validateDate(subjectDTO.getEndTime(), Subject.class);
        LocalDate endTime = LocalDate.parse(subjectDTO.getEndTime());
        if (Period.between(startTime, endTime).getMonths() < 1) {
            throw new SubjectException(SubjectErrors.EndTime_Invalid);
        }
        Validate.validateBlock(subjectDTO.getSubjectBlock(), Subject.class);
        Subject newSubject = new Subject();
        newSubject.loadFromDto(subjectDTO);
        subjectRepository.save(newSubject);
        return newSubject;
    }

    @Transactional
    public Subject updateSubject(Long subjectId, SubjectDTO subjectDTO) {
        Optional<Subject> found = subjectRepository.findById(subjectId);
        if (!found.isPresent()) {
            throw new SubjectException(SubjectErrors.NotFound_Subject);
        }
        if (LocalDate.now().isAfter(found.get().getStartTime())) {
            throw new SubjectException(SubjectErrors.Started_Subject);
        }
        for (Subject s : subjectRepository.findAll()) {
            if (s.getId() != subjectId && s.getName().equalsIgnoreCase(subjectDTO.getName())) {
                throw new SubjectException(SubjectErrors.Name_Exist);
            }
        }
        if (subjectDTO.getName() != null && !subjectDTO.getName().trim().isEmpty() && subjectDTO.getName().length() > 0 && !Objects.equals(found.get().getName(), subjectDTO.getName())) {
            found.get().setName(subjectDTO.getName());
        }
        if (subjectDTO.getCredits() != null && !subjectDTO.getCredits().trim().isEmpty()) {
            Integer credits = Validate.validateCredits(subjectDTO.getCredits());
            found.get().setCredits(credits);
        }

        if (subjectDTO.getStartTime() != null && !subjectDTO.getStartTime().trim().isEmpty()) {
            LocalDate startTime = Validate.validateDate(subjectDTO.getStartTime(), Subject.class);
            if (LocalDate.now().isAfter(startTime)) {
                throw new SubjectException(SubjectErrors.StartTime_Invalid);
            }
            if (Period.between(found.get().getStartTime(), found.get().getEndTime()).getMonths() < 1) {
                throw new SubjectException(SubjectErrors.EndTime_Invalid);
            }
            found.get().setStartTime(startTime);
        }
        if (subjectDTO.getEndTime() != null && !subjectDTO.getEndTime().trim().isEmpty()) {
            LocalDate endTime = Validate.validateDate(subjectDTO.getEndTime(), Subject.class);
            if (Period.between(found.get().getStartTime(), endTime).getMonths() < 1) {
                throw new SubjectException(SubjectErrors.EndTime_Invalid);
            }
            found.get().setEndTime(endTime);
        }
        Block block1 = Validate.validateBlock(subjectDTO.getSubjectBlock(), Subject.class);
        found.get().setSubjectBlock(block1);

        return found.get();
    }


    public void deleteSubject(Long subjectId) {
        Optional<Subject> found = subjectRepository.findById(subjectId);
        if (!found.isPresent()) {
            throw new SubjectException(SubjectErrors.NotFound_Subject);
        }
        subjectRepository.deleteById(subjectId);
    }

    @Transactional
    public Student registerSubjects(Long studentId, Long subjectId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (!student.isPresent()) {
            throw new StudentException(StudentErrors.NotFound_Student);
        }
        Optional<Subject> subject = subjectRepository.findById(subjectId);
        if (!subject.isPresent()) {
            throw new SubjectException(SubjectErrors.NotFound_Subject);
        }
        Subject s = subject.get();
        if (student.get().getSubjects().contains(s)) {
            throw new SubjectException(SubjectErrors.Already_Register);
        }
        if (LocalDate.now().isAfter(subject.get().getEndTime())) {
            throw new SubjectException(SubjectErrors.Over_Subject);
        }
        if (Period.between(subject.get().getStartTime(), LocalDate.now()).getMonths() >= 1) {
            throw new SubjectException(SubjectErrors.Late_Register);
        }
        s.getStudents().add(student.get());
        student.get().getSubjects().add(subject.get());
        return student.get();
    }

    public List<Subject> getSubjectsByStudentId(Long studentId) {
        if(!studentRepository.existsById(studentId)){
            throw new StudentException(StudentErrors.NotFound_Student);
        }
        return studentRepository.findById(studentId).get().getSubjects().stream().toList();
    }

    @Transactional
    public Student unregisterSubject(Long studentId, Long subjectId) {
        if (!studentRepository.existsById(studentId)) {
            throw new StudentException(StudentErrors.NotFound_Student);
        }
        Student student = studentRepository.findById(studentId).get();

        if (!subjectRepository.existsById(subjectId)) {
            throw new SubjectException(SubjectErrors.NotFound_Subject);
        }
        Subject subject = subjectRepository.findById(subjectId).get();

        if (!student.getSubjects().contains(subject)) {
            throw new SubjectException(SubjectErrors.No_Register);
        }

        if (LocalDate.now().isAfter(subject.getStartTime())) {
            throw new SubjectException(SubjectErrors.Started_Subject);
        }

        student.getSubjects().remove(subject);
        subject.getStudents().remove(student);

        return student;
    }

    public Page<Subject> searchSubjects(String subjectBlock, String credits, String startTime, String endTime, String studentName, int currentPage, int pageSize) {
        Specification spec = Specification.where(null);

        if (subjectBlock != null) {
            Block block = Validate.validateBlock(subjectBlock, Subject.class);
            spec = spec.and(SubjectSpecification.bySubjectBlock(block));
        }

        if (credits != null) {
            Integer cre = Validate.validateCredits(credits);
            spec = spec.and(SubjectSpecification.byCredits(cre));
        }

        if (startTime != null) {
            LocalDate start = Validate.validateDate(startTime, Subject.class);
            spec = spec.and(SubjectSpecification.byStartTime(start));
        }

        if (endTime != null) {
            LocalDate end = Validate.validateDate(endTime, Subject.class);
            spec = spec.and(SubjectSpecification.byEndTime(end));
        }

        if (studentName != null) {
            spec = spec.and(StudentSpecification.nameContains(studentName));
        }

        Page<Subject> subjectPage = subjectRepository.findAll(spec, PageRequest.of(currentPage - 1, pageSize));

        return subjectPage;

    }
}
