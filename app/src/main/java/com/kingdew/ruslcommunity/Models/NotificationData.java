package com.kingdew.ruslcommunity.Models;

public class NotificationData {

    private String ID,TITLE,DESC,IMAGE,NOT_DESC,STATUS,FILT;

    public NotificationData() {
    }

    public NotificationData(String ID, String TITLE, String DESC, String IMAGE, String NOT_DESC, String STATUS, String FILT) {
        this.ID = ID;
        this.TITLE = TITLE;
        this.DESC = DESC;
        this.IMAGE = IMAGE;
        this.NOT_DESC = NOT_DESC;
        this.STATUS = STATUS;
        this.FILT = FILT;
    }



    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getDESC() {
        return DESC;
    }

    public void setDESC(String DESC) {
        this.DESC = DESC;
    }

    public String getIMAGE() {
        return IMAGE;
    }

    public void setIMAGE(String IMAGE) {
        this.IMAGE = IMAGE;
    }

    public String getNOT_TITLE() {
        return NOT_DESC;
    }

    public void setNOT_TITLE(String NOT_DESC) {
        this.NOT_DESC = NOT_DESC;
    }

    public String getNOT_DESC() {
        return NOT_DESC;
    }

    public void setNOT_DESC(String NOT_DESC) {
        this.NOT_DESC = NOT_DESC;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getFILT() {
        return FILT;
    }

    public void setFILT(String FILT) {
        this.FILT = FILT;
    }
}
