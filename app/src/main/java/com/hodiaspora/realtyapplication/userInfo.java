package com.hodiaspora.realtyapplication;

public class userInfo {
    private String email;
    private String identification;
    private String phone;

    public userInfo() {
    }

    public userInfo(String email, String identification, String phone) {
        this.email = email;
        this.identification = identification;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
