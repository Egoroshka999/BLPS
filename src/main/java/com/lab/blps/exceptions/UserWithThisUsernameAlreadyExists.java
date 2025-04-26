package com.lab.blps.exceptions;

public class UserWithThisUsernameAlreadyExists extends RuntimeException {
    public UserWithThisUsernameAlreadyExists(String message) {
        super(message);
    }
}
