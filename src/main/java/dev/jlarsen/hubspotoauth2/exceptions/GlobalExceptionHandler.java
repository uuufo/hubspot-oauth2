package dev.jlarsen.hubspotoauth2.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

//    @ExceptionHandler(value = UserExistsException.class)
//    protected ResponseEntity<Object> handleUserExistsException(UserExistsException ex, WebRequest request) {
//        return handleExceptionInternal(ex, ex, new HttpHeaders(), HttpStatus.CONFLICT, request);    }
}
