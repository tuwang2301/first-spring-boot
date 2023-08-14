package com.example.demo.student;

import com.example.demo.enumUsages.Conduct;
import com.example.demo.enumUsages.Gender;
import com.example.demo.enumUsages.Rank;

import java.time.LocalDate;

public interface StudentProjection {
    Long getId();
    String getName();
    LocalDate getDob();
    String getEmail();
    Gender getGender();
    Rank getRank();
    Conduct getConduct();

}
