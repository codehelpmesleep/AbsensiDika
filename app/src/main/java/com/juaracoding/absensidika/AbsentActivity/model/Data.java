
package com.juaracoding.absensidika.AbsentActivity.model;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Serializable, Parcelable
{

    @SerializedName("absent_activity")
    @Expose
    private List<AbsentActivity_> absentActivity = null;
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
    private final static long serialVersionUID = -6520228707330219053L;

    protected Data(Parcel in) {
        in.readList(this.absentActivity, (AbsentActivity_.class.getClassLoader()));
    }

    public Data() {
    }

    public List<AbsentActivity_> getAbsentActivity() {
        return absentActivity;
    }

    public void setAbsentActivity(List<AbsentActivity_> absentActivity) {
        this.absentActivity = absentActivity;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(absentActivity);
    }

    public int describeContents() {
        return  0;
    }

}
