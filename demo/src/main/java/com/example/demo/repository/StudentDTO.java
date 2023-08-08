package com.example.demo.repository;

import com.example.demo.entities.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {
    private String name;
    private String dob;
    private String email;

    public void loadFromEntity(Student student){
        this.name = student.getName();
        this.dob = student.getDob().toString();
        this.email = student.getEmail();
    }
}
