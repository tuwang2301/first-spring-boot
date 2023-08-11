package com.example.demo.errorhandler;

public class ClassException extends RuntimeException {
    private final ClassErrors classErrors;

    public ClassException(ClassErrors classErrors) {
        this.classErrors = classErrors;
    }

    public ClassErrors getClassErrors() {
        return classErrors;
    }
}
