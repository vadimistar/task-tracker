package com.vadimistar.tasktrackerbackend.security.auth;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
