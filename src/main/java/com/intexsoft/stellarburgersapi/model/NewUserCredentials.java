package com.intexsoft.stellarburgersapi.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.github.javafaker.Faker;

public class NewUserCredentials {
    private String email;
    private String name;

    public NewUserCredentials(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public NewUserCredentials() {
    }

    public static NewUserCredentials buildFromNewUser(NewUser newUser) {
        return new NewUserCredentials(newUser.getEmail(), newUser.getName());
    }

    public static NewUserCredentials buildFakeCredentials() {
        Faker faker = new Faker();
        return new NewUserCredentials(faker.internet().emailAddress(), faker.name().username());
    }

    @JsonGetter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonGetter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
