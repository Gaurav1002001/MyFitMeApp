package com.example.myfitmeapp.Model;

public class User {

    private String username;
    private String fullname;
    private String email;
    private String imageurl;
    private String userid;
    private String bio;
    private String phone;
    private String location;
    private String gender;
    private String birthday;
    private String joindate;

    public User() {
    }

    public User(String username , String fullname, String email, String imageurl, String userid, String bio, String phone,
                String location, String gender, String birthday, String joindate) {
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.imageurl = imageurl;
        this.userid = userid;
        this.bio = bio;
        this.phone = phone;
        this.location = location;
        this.gender = gender;
        this.birthday = birthday;
        this.joindate = joindate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getJoindate() {
        return joindate;
    }

    public void setJoindate(String joindate) {
        this.joindate = joindate;
    }
}
