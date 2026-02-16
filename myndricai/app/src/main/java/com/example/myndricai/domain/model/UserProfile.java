package com.example.myndricai.domain.model;

public class UserProfile {
    public String uid;
    public String displayName;

    public UserProfile() {}

    public UserProfile(String uid, String displayName) {
        this.uid = uid;
        this.displayName = displayName;
    }
}
