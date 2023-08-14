package com.example.demo.student;

import com.example.demo.enumUsages.Conduct;
import com.example.demo.enumUsages.Gender;
import com.example.demo.enumUsages.Rank;

import java.util.Arrays;

public enum StudentErrors {
    NotFound_Student("Student not found"),
    Date_Invalid("Dob is invalid"),
    Gender_Invalid("Gender is invalid. Gender has to be one of " + Arrays.toString(Gender.values())),
    Rank_Invalid("Rank is invalid. Rank has to be one of " + Arrays.toString(Rank.values())),
    Conduct_Invalid("Conduct is invalid. Conduct has to be one of " + Arrays.toString(Conduct.values())),
    Email_Taken("Email already exists");
    private String message;

    StudentErrors(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }
}
