package com.example.demo.entities;

import com.example.demo.repository.ClassRoomDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
