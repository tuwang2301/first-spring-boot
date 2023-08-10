package com.example.demo.repository;

import com.example.demo.entities.Student;
import com.example.demo.enumUsages.Conduct;
import com.example.demo.enumUsages.Gender;
import com.example.demo.enumUsages.Rank;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {
    @Schema(required = true)
    private String name;
    @Schema(format = "date-time"
            , example = "1990-01-01"
            , required = true)
    private String dob;
    @Schema(required = true)
    private String email;
    @Schema(oneOf = Gender.class
            , required = true)
    private String gender;
    @Schema(oneOf = Rank.class
            , required = true)
    private String rank;
    @Schema(oneOf = Conduct.class
            , required = true)
    private String conduct;

    public void loadFromEntity(Student student) {
        this.name = student.getName();
        this.dob = student.getDob().toString();
        this.email = student.getEmail();
    }
}
