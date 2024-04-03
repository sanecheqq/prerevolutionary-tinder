package com.liga.semin.server.exception;

public class UserNotFound extends RuntimeException {
    public UserNotFound() {
    }

    public UserNotFound(String message) {
        super(message);
    }
}
