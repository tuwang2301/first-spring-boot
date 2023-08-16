package com.example.demo.user;

public class UserException extends RuntimeException{
    private final UserErrors userErrors;

    public UserException(UserErrors userErrors) {
        this.userErrors = userErrors;
    }

    public UserErrors getUserErrors(){
        return userErrors;
    }
}
