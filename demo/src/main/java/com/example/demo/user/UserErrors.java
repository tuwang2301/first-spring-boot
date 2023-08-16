package com.example.demo.user;

import com.example.demo.enumUsages.RoleName;

import java.util.Arrays;

public enum UserErrors {
    Username_Exist("Username already exist"),
    RoleName_Invalid("RoleName must be one of " + Arrays.toString(RoleName.values()));

    private String message;

    UserErrors(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
