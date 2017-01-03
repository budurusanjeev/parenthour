package parentshour.spinlogics.com.parentshour.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by SPINLOGICS on 1/2/2017.
 */

public class PlayDateEventModelParcel implements Parcelable {
    public static final Creator<PlayDateEventModelParcel> CREATOR = new Creator<PlayDateEventModelParcel>() {
        @Override
        public PlayDateEventModelParcel createFromParcel(Parcel in) {
            return new PlayDateEventModelParcel(in);
        }

        @Override
        public PlayDateEventModelParcel[] newArray(int size) {
            return new PlayDateEventModelParcel[size];
        }
    };
    String name;
    String date;
    String time;
    String pId;
    String imgurl;
    String pEid;
    String address;
    ArrayList playDateMembers;

    public PlayDateEventModelParcel() {

    }

    protected PlayDateEventModelParcel(Parcel in) {
        pEid = in.readString();
        name = in.readString();
        date = in.readString();
        time = in.readString();
        address = in.readString();
        pId = in.readString();
        imgurl = in.readString();

    }

    public static Creator<PlayDateEventModelParcel> getCREATOR() {
        return CREATOR;
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

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

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

    public ArrayList getPlayDateMembers() {
        return playDateMembers;
    }

    public void setPlayDateMembers(ArrayList playDateMembers) {
        this.playDateMembers = playDateMembers;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(pEid);
        parcel.writeString(name);
        parcel.writeString(date);
        parcel.writeString(time);
        parcel.writeString(address);
        parcel.writeString(pId);
        parcel.writeString(imgurl);
    }
}
