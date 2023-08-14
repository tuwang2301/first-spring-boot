package com.example.demo.subject;

public class SubjectException extends RuntimeException{
    private final SubjectErrors subjectErrors;

    public SubjectException(SubjectErrors subjectErrors) {
        this.subjectErrors = subjectErrors;
    }

    public SubjectErrors getSubjectErrors() {
        return subjectErrors;
    }
}
