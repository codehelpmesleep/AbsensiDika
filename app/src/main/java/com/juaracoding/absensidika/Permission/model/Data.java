
package com.juaracoding.absensidika.Permission.model;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Serializable, Parcelable
{

    @SerializedName("permission_category")
    @Expose
    private List<PermissionCategory_> permissionCategory = null;
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
    private final static long serialVersionUID = -7039618622687427114L;

    protected Data(Parcel in) {
        in.readList(this.permissionCategory, (PermissionCategory_.class.getClassLoader()));
    }

    public Data() {
    }

    public List<PermissionCategory_> getPermissionCategory() {
        return permissionCategory;
    }

    public void setPermissionCategory(List<PermissionCategory_> permissionCategory) {
        this.permissionCategory = permissionCategory;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(permissionCategory);
    }

    public int describeContents() {
        return  0;
    }

}
