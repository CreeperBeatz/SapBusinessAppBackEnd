package com.company.exceptions;

public class InvalidEmailException extends Exception {
    @Override
    public String getMessage(){
        return "Email doesn't match regex!";
    }

}

