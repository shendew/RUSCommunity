package com.kingdew.ruslcommunity.Models;

public class EventData {
    int id;
    String title,image;
    boolean isJoined;



    public EventData(int id, String title, String image, boolean isJoined) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.isJoined = isJoined;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public void setJoined(Boolean joined) {
        isJoined = joined;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isJoined() {
        return isJoined;
    }

    public void setJoined(boolean joined) {
        isJoined = joined;
    }
}
