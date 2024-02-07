package com.example.socialnetwork.advice;

import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException exception) {
        Map<String, String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        return errorMap;
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ResponseStatusException.class)
    public String handleResponseStatusException(ResponseStatusException exception) {
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public String handleHttpMessageNotReadableException() {
        return "error: expected format dd/MM/yyyy";
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MultipartException.class)
    public String handleMultipartException() {
        return "error: please choose a file";
    }


    //chua bat dc invalid token
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SignatureException.class)
    public String handleSignatureException() {
        return "error: invalid token";
    }

}
