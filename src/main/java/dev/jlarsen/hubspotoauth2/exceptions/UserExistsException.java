package dev.jlarsen.hubspotoauth2.exceptions;

public class UserExistsException extends Exception {
    public UserExistsException(String message) {
        super(message);
    }
}
