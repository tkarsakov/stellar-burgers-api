package com.intexsoft.stellarburgersapi.model;


import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ExistingUser {
    private String email;
    private String password;
    private Boolean isInvalid = false;

    public ExistingUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public ExistingUser() {
    }

    public static ExistingUser buildFromNewUser(NewUser newUser) {
        return new ExistingUser(newUser.getEmail(), newUser.getPassword());
    }

    @JsonGetter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonGetter
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    public Boolean isInvalid() {
        return isInvalid;
    }

    public void setInvalid(Boolean invalid) {
        isInvalid = invalid;
    }

}
