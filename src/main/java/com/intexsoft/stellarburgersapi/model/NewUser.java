package com.intexsoft.stellarburgersapi.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

public class NewUser {
    private String email;
    private String password;
    private String name;

    public NewUser(String name, String email, String password) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public NewUser() {
    }

    public static NewUser buildFakeUser() {
        Faker faker = new Faker();
        return new NewUser(
                faker.name().username(),
                faker.internet().emailAddress(),
                faker.internet().password()
        );
    }

    public static String getAsJsonString(NewUser newUser) {
        ObjectMapper objectMapper = new ObjectMapper();
        String result;
        try {
            result = objectMapper.writeValueAsString(newUser);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse User object into JSON string due to " + e);
        }
        return result;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
