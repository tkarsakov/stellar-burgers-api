package com.intexsoft.stellarburgersapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.javafaker.Faker;

public class NewUser {
    private String email;
    private String password;
    private String name;
    private Boolean willBeRegistered = true;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    public Boolean getWillBeRegistered() {
        return willBeRegistered;
    }

    public void setWillBeRegistered(Boolean willBeRegistered) {
        this.willBeRegistered = willBeRegistered;
    }
}
