package parentshour.spinlogics.com.parentshour.models;

/**
 * Created by SPINLOGICS on 1/6/2017.
 */

public class ParentEventModel {

    // p_id=12&pe_date=2016-12-24&pe_time=10:30&pe_address=hyderabad&p_id_list=13,12
    String p_id;
    String pe_date;
    String pe_time;
    String pe_address;

    public String getPe_address() {
        return pe_address;
    }

    public void setPe_address(String pe_address) {
        this.pe_address = pe_address;
    }

    public String getPe_time() {
        return pe_time;
    }

    public void setPe_time(String pe_time) {
        this.pe_time = pe_time;
    }

    public String getPe_date() {
        return pe_date;
    }

    public void setPe_date(String pe_date) {
        this.pe_date = pe_date;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }
}
