package com.example.demo.errorhandler;

public enum SubjectErrors {
    NotFound_Subject("Subject not found"),
    Date_Invalid("Date is invalid"),
    StartTime_Invalid("Start time has to be after today and before end time"),
    EndTime_Invalid("End time has to be after start time"),
    Credits_Invalid("Credits is invalid"),
    Credits_Negative("Credits must be more than 0"),
    Block_Invalid("Block is invalid"),
    Name_Exist("This name already exist"),
    Already_Register("Student already register this subject"),
    No_Register("Student have not registered this subject"),
    Over_Subject("This subject is over"),
    Started_Subject("This subject started"),
    Late_Register("This subject started more than a month, so you cannot register");


    private String message;

    SubjectErrors(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
