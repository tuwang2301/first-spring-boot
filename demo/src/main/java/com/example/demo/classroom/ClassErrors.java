package com.example.demo.classroom;

public enum ClassErrors {
    NotFound_Class("ClassRoom not found"),
    Name_Exist("Class name already exist"),
    Wrong_Number_Format("Wrong number format"),
    MaxStudents_Negative("Max student has to be more than 0"),
    MaxStudents_Invalid("Max student has to be higher than students attending"),
    Already_Join("This student already joins"),
    Full_Students("This class is full of students"),
    Block_Invalid("Block is invalid");

    private String message;

    ClassErrors(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }
}
