package parentshour.spinlogics.com.parentshour.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SPINLOGICS on 12/26/2016.
 */

public class ParentFriendModel implements Parcelable {
    public static final Creator<ParentFriendModel> CREATOR = new Creator<ParentFriendModel>() {
        @Override
        public ParentFriendModel createFromParcel(Parcel in) {
            return new ParentFriendModel(in);
        }

        @Override
        public ParentFriendModel[] newArray(int size) {
            return new ParentFriendModel[size];
        }
    };
    String pName;
    String pImgUrl;
    String pId;
    Boolean selectFriend;

    public ParentFriendModel(Parcel in) {
        pName = in.readString();
        pImgUrl = in.readString();
        pId = in.readString();
        selectFriend = Boolean.valueOf(in.readString());
    }

    public ParentFriendModel() {

    }

    public Boolean getSelectFriend() {
        return selectFriend;
    }

    public void setSelectFriend(Boolean selectFriend) {
        this.selectFriend = selectFriend;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpImgUrl() {
        return pImgUrl;
    }

    public void setpImgUrl(String pImgUrl) {
        this.pImgUrl = pImgUrl;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(pName);
        parcel.writeString(pImgUrl);
        parcel.writeString(pId);
        parcel.writeString(String.valueOf(selectFriend));
    }
}
