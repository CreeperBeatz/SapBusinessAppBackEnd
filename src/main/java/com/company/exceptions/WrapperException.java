package com.company.exceptions;

public class WrapperException extends Exception{
    Exception inheritedException;
    String wrapperMessage;

    public WrapperException(Exception e) {
        this.inheritedException = e;
    }
    public WrapperException(Exception e, String message) {
        this.inheritedException = e;
        this.wrapperMessage = message;
    }

    public String getWrapperMessage() {
        return this.wrapperMessage;
    }
    public Exception getInheritedException() {
        return inheritedException;
    }
}
