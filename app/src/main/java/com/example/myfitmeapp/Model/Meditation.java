package com.example.myfitmeapp.Model;

public class Meditation {

    String imageurl;
    String title;
    String time;

    public Meditation() {
    }

    public Meditation(String imageurl, String title, String time) {
        this.imageurl = imageurl;
        this.title = title;
        this.time = time;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
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
}
