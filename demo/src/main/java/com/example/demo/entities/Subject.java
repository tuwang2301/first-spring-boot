package com.example.demo.entities;


import com.example.demo.entities.Student;
import com.example.demo.enumUsages.Block;
import com.example.demo.repository.SubjectDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Locale;

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
    @Schema(required = true)
    private Long id;
    @Column(unique = true)
    @Schema(required = true)
    private String name;
    @Schema(required = true)
    private Block subjectBlock;
    @Schema(required = true)
    private Integer credits;
    @Schema(required = true)
    private LocalDate startTime;
    @Schema(required = true)
    private LocalDate endTime;
    @JsonIgnore
    @ManyToMany
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinTable(
            name = "subject_student",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Collection<Student> students;

//    @OneToOne(mappedBy = "subject")
//    private Teacher teacher;
    public void loadFromDto(SubjectDTO subjectDTO){
        this.name = subjectDTO.getName();
        this.credits = Integer.parseInt(subjectDTO.getCredits());
        this.startTime = LocalDate.parse(subjectDTO.getStartTime());
        this.endTime = LocalDate.parse(subjectDTO.getEndTime());
        this.subjectBlock = Block.valueOf(subjectDTO.getSubjectBlock());
    }
}
