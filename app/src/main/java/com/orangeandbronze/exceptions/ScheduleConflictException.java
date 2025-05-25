package com.orangeandbronze.exceptions;

public class ScheduleConflictException extends EnlistmentException {
    public ScheduleConflictException(String message) {
        super(message);
    }
}