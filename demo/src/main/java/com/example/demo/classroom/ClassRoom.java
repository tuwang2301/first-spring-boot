package com.example.demo.classroom;

import com.example.demo.student.Student;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassRoom {
    @Id
    @SequenceGenerator(
            name = "classroom_sequence",
            sequenceName = "classroom_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "classroom_sequence"
    )
    private Long id;
    private String name;
    private Integer maxStudents;
    @OneToMany(mappedBy = "classRoom", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Collection<Student> students;
}
