package com.example.demo.classroom;

import com.example.demo.student.Student;
import com.example.demo.enumUsages.Block;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(required = true)
    private Long id;
    @Schema(required = true)
    @Column(unique = true)
    private String name;
    @Schema(required = true)
    private Integer maxStudents;
    @Schema(required = true)
    private Block classBlock;
    @JsonIgnore
    @OneToMany(mappedBy = "classRoom", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Collection<Student> students;

    public void loadFromDto(ClassRoomDTO classRoomDTO){
        this.name = classRoomDTO.getName();
        this.maxStudents = Integer.parseInt(classRoomDTO.getMaxStudents());
    }
}
