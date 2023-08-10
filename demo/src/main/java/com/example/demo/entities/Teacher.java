//package com.example.demo.entities;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDate;
//import java.time.Period;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table
//@Builder
//public class Teacher {
//    @Id
//    @SequenceGenerator(
//            name = "teacher_sequence",
//            sequenceName = "teacher_sequence",
//            allocationSize = 1
//    )
//    @GeneratedValue(
//            strategy = GenerationType.SEQUENCE,
//            generator = "teacher_sequence"
//    )
//    private Long id;
//    private String name;
//    private LocalDate dob;
//    @Transient
//    private Integer age;
//    private String email;
//
//    @OneToOne
//    @JoinColumn(name = "subject_id")
//    private Subject subject;
//    public Integer getAge() {
//        return Period.between(dob, LocalDate.now()).getYears();
//    }
//
//}
