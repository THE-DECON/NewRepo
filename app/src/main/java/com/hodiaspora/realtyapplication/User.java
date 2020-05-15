package com.hodiaspora.realtyapplication;

public class User {
    private String email;
    private String name;
    private String phone;
    private String identification;

    public User(String email, String password) {
        this.email = email;
    }

    public User(String email, String name, String phone, String identification) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.identification = identification;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }
}
