package com.example.demo.subject;


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
import java.time.LocalDate;
import java.util.Collection;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
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
    @ManyToOne
    @JoinColumn(name = "created_by")
    @CreatedBy
    private ApplicationUser createdBy;

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
