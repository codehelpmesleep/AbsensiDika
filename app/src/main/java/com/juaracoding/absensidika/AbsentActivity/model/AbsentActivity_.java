
package com.juaracoding.absensidika.AbsentActivity.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AbsentActivity_ implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("manager_id")
    @Expose
    private String managerId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("lattiude")
    @Expose
    private String lattiude;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("absent_type")
    @Expose
    private String absentType;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("qr_number")
    @Expose
    private String qrNumber;
    public final static Creator<AbsentActivity_> CREATOR = new Creator<AbsentActivity_>() {


        @SuppressWarnings({
            "unchecked"
        })
        public AbsentActivity_ createFromParcel(Parcel in) {
            return new AbsentActivity_(in);
        }

        public AbsentActivity_[] newArray(int size) {
            return (new AbsentActivity_[size]);
        }

    }
    ;
    private final static long serialVersionUID = -9165025504585023851L;

    protected AbsentActivity_(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.managerId = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.dateTime = ((String) in.readValue((String.class.getClassLoader())));
        this.lattiude = ((String) in.readValue((String.class.getClassLoader())));
        this.address = ((String) in.readValue((String.class.getClassLoader())));
        this.longitude = ((String) in.readValue((String.class.getClassLoader())));
        this.absentType = ((String) in.readValue((String.class.getClassLoader())));
        this.picture = ((String) in.readValue((String.class.getClassLoader())));
        this.qrNumber = ((String) in.readValue((String.class.getClassLoader())));
    }

    public AbsentActivity_() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLattiude() {
        return lattiude;
    }

    public void setLattiude(String lattiude) {
        this.lattiude = lattiude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAbsentType() {
        return absentType;
    }

    public void setAbsentType(String absentType) {
        this.absentType = absentType;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getQrNumber() {
        return qrNumber;
    }

    public void setQrNumber(String qrNumber) {
        this.qrNumber = qrNumber;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(userId);
        dest.writeValue(managerId);
        dest.writeValue(status);
        dest.writeValue(dateTime);
        dest.writeValue(lattiude);
        dest.writeValue(address);
        dest.writeValue(longitude);
        dest.writeValue(absentType);
        dest.writeValue(picture);
        dest.writeValue(qrNumber);
    }

    public int describeContents() {
        return  0;
    }

}
