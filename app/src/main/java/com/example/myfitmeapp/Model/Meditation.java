package com.example.myfitmeapp.Model;

public class Meditation {

    String imageurl;
    String backImageurl;
    String title;
    String time;
    String id;
    String description;
    String audiourl;
    String audioFilename;

    public Meditation() {
    }

    public Meditation(String imageurl, String backImageurl, String title, String time, String id, String description, String audiourl, String audioFilename) {
        this.imageurl = imageurl;
        this.backImageurl = backImageurl;
        this.title = title;
        this.time = time;
        this.id = id;
        this.description = description;
        this.audiourl = audiourl;
        this.audioFilename = audioFilename;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getBackImageurl() {
        return backImageurl;
    }

    public void setBackImageurl(String backImageurl) {
        this.backImageurl = backImageurl;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAudiourl() {
        return audiourl;
    }

    public void setAudiourl(String audiourl) {
        this.audiourl = audiourl;
    }

    public String getAudioFilename() {
        return audioFilename;
    }

    public void setAudioFilename(String audioFilename) {
        this.audioFilename = audioFilename;
    }
}
