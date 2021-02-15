package dev.jlarsen.mvcthymeleafdemo.exceptions;

public class UserExistsException extends Exception {
    public UserExistsException(String message) {
        super(message);
    }
}
