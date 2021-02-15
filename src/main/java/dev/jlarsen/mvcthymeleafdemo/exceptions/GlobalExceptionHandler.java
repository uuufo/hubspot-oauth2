package dev.jlarsen.mvcthymeleafdemo.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

//    @ExceptionHandler(value = UserExistsException.class)
//    protected ResponseEntity<Object> handleUserExistsException(UserExistsException ex, WebRequest request) {
//        return handleExceptionInternal(ex, ex, new HttpHeaders(), HttpStatus.CONFLICT, request);    }
}
