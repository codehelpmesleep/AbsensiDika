package com.juaracoding.absensidika.CheckIn.activity.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QrCodeManager implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("qr_number")
    @Expose
    private String qrNumber;
    @SerializedName("expired")
    @Expose
    private String expired;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("manager")
    @Expose
    private String manager;
    public final static Parcelable.Creator<QrCodeManager> CREATOR = new Creator<QrCodeManager>() {


        @SuppressWarnings({
                "unchecked"
        })
        public QrCodeManager createFromParcel(Parcel in) {
            return new QrCodeManager(in);
        }

        public QrCodeManager[] newArray(int size) {
            return (new QrCodeManager[size]);
        }

    }
            ;
    private final static long serialVersionUID = 6221681938579819222L;

    protected QrCodeManager(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.qrNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.expired = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.manager = ((String) in.readValue((String.class.getClassLoader())));
    }

    public QrCodeManager() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQrNumber() {
        return qrNumber;
    }

    public void setQrNumber(String qrNumber) {
        this.qrNumber = qrNumber;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(qrNumber);
        dest.writeValue(expired);
        dest.writeValue(status);
        dest.writeValue(manager);
    }

    public int describeContents() {
        return 0;
    }

}