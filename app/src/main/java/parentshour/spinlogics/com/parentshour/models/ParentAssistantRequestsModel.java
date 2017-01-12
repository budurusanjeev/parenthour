package parentshour.spinlogics.com.parentshour.models;

/**
 * Created by SPINLOGICS on 1/11/2017.
 */

public class ParentAssistantRequestsModel {
    /*"a_id": "3",
            "a_name": "Govind Katla",
            "a_pic": "http://spinlogics.com/Parentshour/uploads/1480486583.6864.png",
            "a_task_name": "bbaa",
            "a_date": "2017-11-01",
            "a_start_time": "15:00:00",
            "a_end_time": "24:00:00",
            "a_req_id": "2",
            "a_status": "pending"*/

    String a_id;
    String a_name;
    String a_pic;
    String a_task_name;
    String a_date;
    String a_start_time;
    String a_end_time;
    String a_req_id;
    String a_status;

    public String getA_status() {
        return a_status;
    }

    public void setA_status(String a_status) {
        this.a_status = a_status;
    }

    public String getA_req_id() {
        return a_req_id;
    }

    public void setA_req_id(String a_req_id) {
        this.a_req_id = a_req_id;
    }

    public String getA_end_time() {
        return a_end_time;
    }

    public void setA_end_time(String a_end_time) {
        this.a_end_time = a_end_time;
    }

    public String getA_start_time() {
        return a_start_time;
    }

    public void setA_start_time(String a_start_time) {
        this.a_start_time = a_start_time;
    }

    public String getA_date() {
        return a_date;
    }

    public void setA_date(String a_date) {
        this.a_date = a_date;
    }

    public String getA_task_name() {
        return a_task_name;
    }

    public void setA_task_name(String a_task_name) {
        this.a_task_name = a_task_name;
    }

    public String getA_pic() {
        return a_pic;
    }

    public void setA_pic(String a_pic) {
        this.a_pic = a_pic;
    }

    public String getA_name() {
        return a_name;
    }

    public void setA_name(String a_name) {
        this.a_name = a_name;
    }

    public String getA_id() {
        return a_id;
    }

    public void setA_id(String a_id) {
        this.a_id = a_id;
    }
}
