package com.example.demo.classroom;

import com.example.demo.student.Student;
import com.example.demo.enumUsages.Block;
import com.example.demo.user.ApplicationUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.util.Collection;

@Data
@Entity
@Table(name = "class_room")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
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

//    @ManyToOne
//    @JoinColumn(name = "created_by")
//    @CreatedBy
//    private ApplicationUser createdBy;

    @CreatedDate
    @Column(name = "created_at")
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    @LastModifiedBy
    private ApplicationUser updatedBy;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Timestamp updatedAt;

    public void loadFromDto(ClassRoomDTO classRoomDTO){
        this.name = classRoomDTO.getName();
        this.maxStudents = Integer.parseInt(classRoomDTO.getMaxStudents());
    }
}
