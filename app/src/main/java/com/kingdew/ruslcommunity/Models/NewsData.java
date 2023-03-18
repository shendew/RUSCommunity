package com.kingdew.ruslcommunity.Models;

public class NewsData {
    String header,date,body,link;

    public NewsData(String header, String date, String body,String link) {
        this.header = header;
        this.date = date;
        this.body = body;
        this.link=link;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
