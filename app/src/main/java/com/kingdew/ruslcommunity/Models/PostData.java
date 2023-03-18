package com.kingdew.ruslcommunity.Models;

public class PostData {
    private String id;
    private String url;
    private String title;
    private String auther;
    private String user;
    private String desc;

    public PostData() {
    }

    public PostData(String auther, String id, String title, String url, String user, String desc) {
        this.id=id;
        this.url = url;
        this.title = title;
        this.auther = auther;
        this.user=user;
        this.desc=desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuther() {
        return auther;
    }

    public void setAuther(String auther) {
        this.auther = auther;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
