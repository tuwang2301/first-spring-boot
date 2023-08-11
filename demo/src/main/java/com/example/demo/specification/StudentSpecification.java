package com.example.demo.specification;

import com.example.demo.entities.*;
import com.example.demo.enumUsages.Block;
import com.example.demo.enumUsages.Conduct;
import com.example.demo.enumUsages.Gender;
import com.example.demo.enumUsages.Rank;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.function.Predicate;

public class StudentSpecification {
    public static Specification<Student> inClass(String className) {
        return (student, cq, cb) -> {
            Join<Student, ClassRoom> classRoomJoin = student.join(Student_.CLASS_ROOM);
            return cb.equal(classRoomJoin.get(ClassRoom_.NAME), className);
        };
    }
    public static Specification<Student> joinSubject(String subjectName) {
        return (student, cq, cb) -> {
            Join<Student, Subject> subjectJoin = student.join(Student_.SUBJECTS);
            return cb.equal(subjectJoin.get(Subject_.NAME), subjectName);
        };
    }

    public static Specification<Student> hasSex(Gender gender) {
        return (student, cq, cb) -> {
            return cb.equal(student.get(Student_.GENDER), gender);
        };
    }

    public static Specification<Student> hasRank(Rank rank) {
        return (student, cq, cb) -> {
            return cb.equal(student.get(Student_.RANK), rank);
        };
    }

    public static Specification<Student> hasConduct(Conduct conduct) {
        return (student, cq, cb) -> {
            return cb.equal(student.get(Student_.CONDUCT), conduct);
        };
    }

    public static Specification<Student> classBlock(Block classBlock) {
        return (student, cq, cb) -> {
            Join<Student, ClassRoom> classRoomJoin = student.join(Student_.CLASS_ROOM);
            return cb.equal(classRoomJoin.get(ClassRoom_.CLASS_BLOCK), classBlock);
        };
    }
    public static Specification<Student> subjectBlock(Block subjectBlock) {
        return (student, cq, cb) -> {
            Join<Student, Subject> subjectJoin = student.join(Student_.SUBJECTS);
            return cb.equal(subjectJoin.get(Subject_.SUBJECT_BLOCK), subjectBlock);
        };
    }

    public static Specification nameContains(String studentName) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(criteriaBuilder.lower(root.get(Student_.NAME)), "%" + studentName.toLowerCase() + "%");
        };
    }
}
