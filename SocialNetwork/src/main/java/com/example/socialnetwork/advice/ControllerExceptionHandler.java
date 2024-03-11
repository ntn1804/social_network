package com.example.socialnetwork.advice;

import com.example.socialnetwork.exception.GeneralException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
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
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
        return errorMap;
    }

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<?> handleGeneralException(GeneralException exception) {
        return ResponseEntity.status(exception.getStatusCode())
                .body(exception.getResponseMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public String handleHttpMessageNotReadableException() {
        return "error: expected format dd/MM/yyyy";
    }

    //chua bat dc invalid token
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SignatureException.class)
    public String handleSignatureException() {
        return "error: invalid token";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidMediaTypeException.class)
    public String handleInvalidMediaTypeException(InvalidMediaTypeException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<String> handleInvalidPostStatus() {
        return new ResponseEntity<>("Invalid post status. Please choose: 1. PUBLIC, 2. FRIENDS, 3. PRIVATE",
                HttpStatus.BAD_REQUEST);
    }
}
