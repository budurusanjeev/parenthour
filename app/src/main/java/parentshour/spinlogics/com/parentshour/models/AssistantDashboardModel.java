package parentshour.spinlogics.com.parentshour.models;

/**
 * Created by SPINLOGICS on 12/20/2016.
 */

public class AssistantDashboardModel {
    String title;
    String date;
    String time;
    String status;
    String name;
    String imgUrl;
    String pId;
    String a_req_id;
    String endtime;
    String pName;

    public String getA_req_id() {
        return a_req_id;
    }

    public void setA_req_id(String a_req_id) {
        this.a_req_id = a_req_id;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imhUrl) {
        this.imgUrl = imhUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
