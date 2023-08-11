package com.example.demo.entities;

import com.example.demo.enumUsages.Conduct;
import com.example.demo.enumUsages.Gender;
import com.example.demo.enumUsages.Rank;
import com.example.demo.repository.StudentDTO;
import com.example.demo.validate.ValidEnumValue;
import com.example.demo.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.Random;

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
    @Schema(required = true)
    private Long id;
    @JsonView({Views.StudentWithoutClass.class, Views.StudentWithoutSubject.class})
    @Schema(required = true)
    private String name;
    @JsonView({Views.StudentWithoutClass.class, Views.StudentWithoutSubject.class})
    @Schema(required = true)
    private LocalDate dob;
    @Transient
    @JsonView({Views.StudentWithoutClass.class, Views.StudentWithoutSubject.class})
    private Integer age;
    @Column(unique = true)
    @Schema(required = true)
    @JsonView({Views.StudentWithoutClass.class, Views.StudentWithoutSubject.class})
    private String email;
    @Schema(required = true)
    @JsonView({Views.StudentWithoutClass.class, Views.StudentWithoutSubject.class})
    @ValidEnumValue(message = "Gender is invalid", enumClass = Gender.class, allowedValues = {"Male", "Female", "Unknown"})
    private Gender gender;
    @Schema(required = true)
    @JsonView({Views.StudentWithoutClass.class, Views.StudentWithoutSubject.class})
    private Rank rank;
    @Schema(required = true)
    @JsonView({Views.StudentWithoutClass.class, Views.StudentWithoutSubject.class})
    private Conduct conduct;


    @ManyToOne
    @JoinColumn(name = "classRoom_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private ClassRoom classRoom;

    @ManyToMany
    @EqualsAndHashCode.Exclude
    private Collection<Subject> subjects;

    public Integer getAge() {
        return Period.between(dob, LocalDate.now()).getYears();
    }

    public void loadFromDto(StudentDTO studentDTO) {
        this.name = studentDTO.getName();
        this.dob = LocalDate.parse(studentDTO.getDob());
        this.email = studentDTO.getEmail();
        this.gender = Gender.valueOf(studentDTO.getGender());
        this.rank = Rank.valueOf(studentDTO.getRank());
        this.conduct = Conduct.valueOf(studentDTO.getConduct());
    }
}
