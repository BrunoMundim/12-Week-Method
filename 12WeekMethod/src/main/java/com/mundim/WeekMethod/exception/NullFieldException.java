package com.mundim.WeekMethod.exception;

public class NullFieldException extends RuntimeException {

    public NullFieldException(String message) {
        super(message);
    }

    public NullFieldException(String message, Throwable cause) {
        super(message, cause);
    }

}
