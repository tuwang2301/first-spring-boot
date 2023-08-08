package com.example.demo.student;

import com.example.demo.classroom.ClassRoom;
import com.example.demo.classroom.ClassRoomController;
import com.example.demo.subject.Subject;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;

@Entity
@Table
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

    public Student() {
    }
    public Student(Long id, String name, LocalDate dob, String email) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.email = email;
    }

    public Student(String name, LocalDate dob, String email, ClassRoom classRoom) {
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.classRoom = classRoom;
    }

    public Student(String name, LocalDate dob, String email) {
        this.name = name;
        this.dob = dob;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Integer getAge() {
        return Period.between(this.dob, LocalDate.now()).getYears();
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setClassRoom(ClassRoom classRoom) {
        this.classRoom = classRoom;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dob=" + dob +
                ", age=" + age +
                ", email='" + email + '\'' +
                '}';
    }
}
