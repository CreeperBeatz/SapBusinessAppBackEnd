package com.company.exceptions;

public class UserDoesNotExistException extends Exception{
    @Override
    public String getMessage() {
        return "User ID doesn't exist";
    }
}
