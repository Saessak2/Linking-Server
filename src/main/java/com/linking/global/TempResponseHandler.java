package com.linking.global;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class TempResponseHandler<T> {

    public static ResponseEntity<Object> generateResponse(HttpStatus status, Object resObj) {

        Map<String, Object> map = new HashMap<>();
        map.put("data", resObj);

        return new ResponseEntity<>(map, status);
    }

}
