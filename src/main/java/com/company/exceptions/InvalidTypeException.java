package com.company.exceptions;

public class InvalidTypeException extends Exception{
    @Override
    public String getMessage() {
        return "Invalid type of user!";
    }
}
