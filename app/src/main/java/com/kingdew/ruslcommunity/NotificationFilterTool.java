package com.kingdew.ruslcommunity;

public class NotificationFilterTool {
    String ret;

    public String NotificationFilterTool(String year, String fac, String event){

        if (year.equals("All Years")&&fac.equals("All Faculties")&&event.equals("All Eevents")){
            ret="all";
        }else {
            ret=year+","+fac+","+event;
        }


        return ret;
    }
}
