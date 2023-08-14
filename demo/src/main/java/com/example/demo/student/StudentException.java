package com.example.demo.student;

public class StudentException extends RuntimeException{
    private final StudentErrors studentErrors;


    public StudentException(StudentErrors studentErrors) {
        super(studentErrors.getMessage());
        this.studentErrors = studentErrors;
    }

    public StudentErrors getStudentErrors(){
        return studentErrors;
    }
}
