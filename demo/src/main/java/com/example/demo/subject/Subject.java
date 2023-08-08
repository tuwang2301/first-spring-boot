package com.example.demo.subject;


import com.example.demo.student.Student;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subject {
    @Id
    @SequenceGenerator(
            name = "subject_sequence",
            sequenceName = "subject_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "subject_sequence"
    )
    private Long id;

    private String name;
    private Integer credits;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinTable(
            name = "subject_student",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Collection<Student> students;
}
