package com.nexware.orderplacement.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public static UserNotFoundException withId(Long userId) {
        return new UserNotFoundException("User not found with id: " + userId);
    }
}
