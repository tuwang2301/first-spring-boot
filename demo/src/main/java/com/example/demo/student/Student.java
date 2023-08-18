package com.example.demo.student;

import com.example.demo.classroom.ClassRoom;
import com.example.demo.subject.Subject;
import com.example.demo.enumUsages.Conduct;
import com.example.demo.enumUsages.Gender;
import com.example.demo.enumUsages.Rank;
import com.example.demo.user.ApplicationUser;
import com.example.demo.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
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
import java.time.Period;
import java.util.Collection;

@Entity
@Table
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
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
    @Schema(required = true)
    private Long id;
    @Schema(required = true)
    private String name;
    @Schema(required = true)
    private LocalDate dob;
    @Transient
    private Integer age;
    @Column(unique = true)
    @Schema(required = true)
    private String email;
    @Schema(required = true)
    private Gender gender;
    @Schema(required = true)
    private Rank rank;
    @Schema(required = true)
    private Conduct conduct;


    @ManyToOne
    @JoinColumn(name = "classRoom_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private ClassRoom classRoom;

    @ManyToMany
    @EqualsAndHashCode.Exclude
    private Collection<Subject> subjects;

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

    public Integer getAge() {
        return Period.between(dob, LocalDate.now()).getYears();
    }

    public void loadFromDto(StudentDTO studentDTO) {
        this.name = studentDTO.getName();
        this.dob = LocalDate.parse(studentDTO.getDob());
        this.email = studentDTO.getEmail();
//        this.gender = Gender.valueOf(studentDTO.getGender());
        this.rank = Rank.valueOf(studentDTO.getRank());
        this.conduct = Conduct.valueOf(studentDTO.getConduct());
    }
}
