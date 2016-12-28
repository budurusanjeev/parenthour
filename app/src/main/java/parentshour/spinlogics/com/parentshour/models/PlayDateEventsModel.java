package parentshour.spinlogics.com.parentshour.models;

import java.util.ArrayList;

/**
 * Created by SPINLOGICS on 12/28/2016.
 */

public class PlayDateEventsModel {
    String name;
    String date;
    String time;
    String pId;
    String imgurl;
    String pEid;
    ArrayList playDateMembers;

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getpEid() {
        return pEid;
    }

    public void setpEid(String pEid) {
        this.pEid = pEid;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList getPlayDateMembers() {
        return playDateMembers;
    }

    public void setPlayDateMembers(ArrayList playDateMembers) {
        this.playDateMembers = playDateMembers;
    }
}
