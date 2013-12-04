package com.bscl.processor;

public class InvalidMessageException extends RuntimeException {

    private static final long serialVersionUID = -1455198079602288033L;

    public InvalidMessageException(String message) {
        super(message);
    }
}
