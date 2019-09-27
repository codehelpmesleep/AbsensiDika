
package com.juaracoding.absensidika.CheckIn.activity.model;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Serializable, Parcelable
{

    @SerializedName("qr_code_manager")
    @Expose
    private List<QrCodeManager> qrCodeManager = null;
    public final static Creator<Data> CREATOR = new Creator<Data>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return (new Data[size]);
        }

    }
    ;
    private final static long serialVersionUID = -9184145329865026654L;

    protected Data(Parcel in) {
        in.readList(this.qrCodeManager, (com.juaracoding.absensidika.CheckIn.activity.model.QrCodeManager.class.getClassLoader()));
    }

    public Data() {
    }

    public List<QrCodeManager> getQrCodeManager() {
        return qrCodeManager;
    }

    public void setQrCodeManager(List<QrCodeManager> qrCodeManager) {
        this.qrCodeManager = qrCodeManager;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(qrCodeManager);
    }

    public int describeContents() {
        return  0;
    }

}
