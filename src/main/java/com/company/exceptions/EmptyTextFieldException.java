package com.company.exceptions;

public class EmptyTextFieldException extends Exception{

    @Override
    public String getMessage(){
        return "User didn't input info on all text fields";
    }
}
