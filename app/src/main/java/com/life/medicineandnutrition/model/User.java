package com.life.medicineandnutrition.model;

public class User {
    private String uuid;
    private String username;
    private String uf;
    private String profileUrl;

    public User() {

    }

    public User(String uuid, String username, String profileUrl, String uf) {
        this.uuid = uuid;
        this.username = username;
        this.uf = uf;
        this.profileUrl = profileUrl;
    }

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getUf() {
        return uf;
    }

    public String getProfileUrl() {
        return profileUrl;
    }
}
