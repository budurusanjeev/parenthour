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

    public String getImhUrl() {
        return imgUrl;
    }

    public void setImhUrl(String imhUrl) {
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
