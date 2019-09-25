
package com.juaracoding.absensidika.Permission.model.approval;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Serializable, Parcelable
{

    @SerializedName("permission_activity")
    @Expose
    private List<PermissionActivity> permissionActivity = null;
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
    private final static long serialVersionUID = 2061752696729586402L;

    protected Data(Parcel in) {
        in.readList(this.permissionActivity, (com.juaracoding.absensidika.Permission.model.approval.PermissionActivity.class.getClassLoader()));
    }

    public Data() {
    }

    public List<PermissionActivity> getPermissionActivity() {
        return permissionActivity;
    }

    public void setPermissionActivity(List<PermissionActivity> permissionActivity) {
        this.permissionActivity = permissionActivity;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(permissionActivity);
    }

    public int describeContents() {
        return  0;
    }

}
