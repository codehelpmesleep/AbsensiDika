
package com.juaracoding.absensidika.Permission.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PermissionCategory_ implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("permission")
    @Expose
    private String permission;
    public final static Creator<PermissionCategory_> CREATOR = new Creator<PermissionCategory_>() {


        @SuppressWarnings({
            "unchecked"
        })
        public PermissionCategory_ createFromParcel(Parcel in) {
            return new PermissionCategory_(in);
        }

        public PermissionCategory_[] newArray(int size) {
            return (new PermissionCategory_[size]);
        }

    }
    ;
    private final static long serialVersionUID = 71210735635850583L;

    protected PermissionCategory_(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.permission = ((String) in.readValue((String.class.getClassLoader())));
    }

    public PermissionCategory_() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(permission);
    }

    public int describeContents() {
        return  0;
    }

}
