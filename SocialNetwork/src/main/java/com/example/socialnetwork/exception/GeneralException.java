package com.example.socialnetwork.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
public class GeneralException extends RuntimeException {
    private HttpStatusCode statusCode;
    private String message;

    public Map<String, String> getResponseMessage() {
        var map = new HashMap<String, String>();
        map.put("message", message);
        return map;
    }
}
