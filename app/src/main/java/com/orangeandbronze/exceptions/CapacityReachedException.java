package com.orangeandbronze.exceptions;

public class CapacityReachedException extends EnlistmentException {
    public CapacityReachedException(String message) {
        super(message);
    }
}