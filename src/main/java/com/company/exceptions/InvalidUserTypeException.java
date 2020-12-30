package com.company.exceptions;

public class InvalidUserTypeException extends Exception{
    @Override
    public String getMessage() {
        return "Invalid type of user!";
    }
}
