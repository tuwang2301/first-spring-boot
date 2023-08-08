package com.example.demo.entities;

import com.example.demo.repository.StudentDTO;
import com.example.demo.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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
    @JsonView({Views.StudentWithoutClass.class, Views.StudentWithoutSubject.class})
    private Long id;
    @JsonView({Views.StudentWithoutClass.class, Views.StudentWithoutSubject.class})
    private String name;
    @JsonView({Views.StudentWithoutClass.class, Views.StudentWithoutSubject.class})
    private LocalDate dob;
    @Transient
    @JsonView({Views.StudentWithoutClass.class, Views.StudentWithoutSubject.class})
    private Integer age;
    @Column(unique = true)
    @JsonView({Views.StudentWithoutClass.class, Views.StudentWithoutSubject.class})
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

    public void loadFromDto(StudentDTO studentDTO){
        this.name = studentDTO.getName();
        this.dob = LocalDate.parse(studentDTO.getDob());
        this.email = studentDTO.getEmail();
    }
}
