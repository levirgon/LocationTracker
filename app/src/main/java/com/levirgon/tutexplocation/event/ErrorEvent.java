package com.levirgon.tutexplocation.event;

/**
 * Created by noushad on 10/27/17.
 */

public class ErrorEvent {
    private final String errorMessage;

    public ErrorEvent(String string) {
        this.errorMessage = string;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
