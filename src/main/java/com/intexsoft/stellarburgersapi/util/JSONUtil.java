package com.intexsoft.stellarburgersapi.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JSONUtil {
    public static String convertToJSONString(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Cannot parse object into JSON " + e.getMessage());
        }
    }

    public static String readJsonFromPath(String path) {
        String response;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            response = objectMapper.readTree(new File(path)).toString();
        } catch (IOException e) {
            throw new RuntimeException("Cannot read response file at " + path);
        }
        return response;
    }
}
