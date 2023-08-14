package com.example.demo.classroom;

public class ClassException extends RuntimeException {
    private final ClassErrors classErrors;

    public ClassException(ClassErrors classErrors) {
        this.classErrors = classErrors;
    }

    public ClassErrors getClassErrors() {
        return classErrors;
    }
}
