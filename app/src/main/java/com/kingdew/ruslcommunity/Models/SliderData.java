package com.kingdew.ruslcommunity.Models;


public class SliderData {

    // image url is used to
    // store the url of image
    private String imgUrl;
    private String title;
    private int id;

    // Constructor method.


    public SliderData(String imgUrl, String title, int id) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.id = id;
    }

    // Getter method
    public String getImgUrl() {
        return imgUrl;
    }

    // Setter method
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
