package com.example.demo.student;

import com.example.demo.classroom.ClassRoom;
import com.example.demo.classroom.ClassRoomController;
import com.example.demo.subject.Subject;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;

@Entity
@Table
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    @Id
    @SequenceGenerator(
            name = "student_sequence",
            sequenceName = "student_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "student_sequence"
    )
    private Long id;
    private String name;
    private LocalDate dob;
    @Transient
    private Integer age;
    @Column(unique = true)
    private String email;

    @ManyToOne
    @JoinColumn(name = "classRoom_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private ClassRoom classRoom;

    @ManyToMany(mappedBy = "students")
    @EqualsAndHashCode.Exclude
    private Collection<Subject> subjects;

    public Integer getAge() {
        return Period.between(dob, LocalDate.now()).getYears();
    }
}
