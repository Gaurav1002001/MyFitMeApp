package com.example.myfitmeapp.FriendsModel;

public class User {

    private String userName;
    private String fullName;
    private String email;
    private String imageUrl;
    private String userId;

    public User() {
    }

    public User(String userName , String fullName, String email, String imageUrl, String userId) {
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.imageUrl = imageUrl;
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
