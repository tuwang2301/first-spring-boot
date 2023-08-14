package com.example.demo.subject;

import com.example.demo.student.Student;
import com.example.demo.enumUsages.Block;
import com.example.demo.student.Student_;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class SubjectSpecification {
    public static Specification<Subject> bySubjectBlock(Block subjectBlock){
        return (subjectRoot, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(subjectRoot.get(Subject_.SUBJECT_BLOCK), subjectBlock);
        };
    }

    public static Specification<Subject> byCredits(Integer credits){
        return (subjectRoot, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(subjectRoot.get(Subject_.CREDITS), credits);
        };
    }

    public static Specification<Subject> byStartTime(LocalDate startTime){
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get(Subject_.START_TIME), startTime);
        };
    }

    public static Specification<Subject> byEndTime(LocalDate endTime){
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get(Subject_.END_TIME), endTime);
        };
    }

    public static Specification<Subject> byStudentName(String studentName){
        return (root, query, criteriaBuilder) -> {
            Join<Subject, Student> subjectStudentJoin = root.join(Subject_.STUDENTS);
            return criteriaBuilder.equal(subjectStudentJoin.get(Student_.NAME),studentName);
        };
    }

}
