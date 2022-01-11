package com.example.myfitmeapp.Model;

public class Post {

    String imageUrl;
    String description;
    String title;
    String time;
    String calorie;
    String postDate;
    String postid;
    String publisher;
    String lusername;
    String lcomment;

    public Post() {
    }

    public Post(String imageUrl, String description, String title, String time, String calorie, String postDate, String postid, String publisher, String lusername, String lcomment) {
        this.imageUrl = imageUrl;
        this.description = description;
        this.title = title;
        this.time = time;
        this.calorie = calorie;
        this.postDate = postDate;
        this.postid = postid;
        this.publisher = publisher;
        this.lcomment = lcomment;
        this.lusername = lusername;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getLusername() {
        return lusername;
    }

    public void setLusername(String lusername) {
        this.lusername = lusername;
    }

    public String getLcomment() {
        return lcomment;
    }

    public void setLcomment(String lcomment) {
        this.lcomment = lcomment;
    }
}
