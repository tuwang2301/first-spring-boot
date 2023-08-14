package com.example.demo.student;

import com.example.demo.classroom.ClassRoom;
import com.example.demo.classroom.ClassRoom_;
import com.example.demo.enumUsages.Block;
import com.example.demo.enumUsages.Conduct;
import com.example.demo.enumUsages.Gender;
import com.example.demo.enumUsages.Rank;
import com.example.demo.subject.Subject;
import com.example.demo.subject.Subject_;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class StudentSpecification {
    public static Specification<StudentProjection> inClass(String className) {
        return (student, cq, cb) -> {
            Join<Student, ClassRoom> classRoomJoin = student.join(Student_.CLASS_ROOM);
            return cb.equal(classRoomJoin.get(ClassRoom_.NAME), className);
        };
    }
    public static Specification<StudentProjection> joinSubject(String subjectName) {
        return (student, cq, cb) -> {
            Join<Student, Subject> subjectJoin = student.join(Student_.SUBJECTS);
            return cb.equal(subjectJoin.get(Subject_.NAME), subjectName);
        };
    }

    public static Specification<StudentProjection> hasSex(Gender gender) {
        return (student, cq, cb) -> {
            return cb.equal(student.get(Student_.GENDER), gender);
        };
    }

    public static Specification<StudentProjection> hasRank(Rank rank) {
        return (student, cq, cb) -> {
            return cb.equal(student.get(Student_.RANK), rank);
        };
    }

    public static Specification<StudentProjection> hasConduct(Conduct conduct) {
        return (student, cq, cb) -> {
            return cb.equal(student.get(Student_.CONDUCT), conduct);
        };
    }

    public static Specification<StudentProjection> classBlock(Block classBlock) {
        return (student, cq, cb) -> {
            Join<Student, ClassRoom> classRoomJoin = student.join(Student_.CLASS_ROOM);
            return cb.equal(classRoomJoin.get(ClassRoom_.CLASS_BLOCK), classBlock);
        };
    }
    public static Specification<StudentProjection> subjectBlock(Block subjectBlock) {
        return (student, cq, cb) -> {
            Join<Student, Subject> subjectJoin = student.join(Student_.SUBJECTS);
            return cb.equal(subjectJoin.get(Subject_.SUBJECT_BLOCK), subjectBlock);
        };
    }

    public static Specification<StudentProjection> nameContains(String studentName) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(criteriaBuilder.lower(root.get(Student_.NAME)), "%" + studentName.toLowerCase() + "%");
        };
    }
}
