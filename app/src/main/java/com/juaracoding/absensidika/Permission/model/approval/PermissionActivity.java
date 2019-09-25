
package com.juaracoding.absensidika.Permission.model.approval;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PermissionActivity implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("manager_id")
    @Expose
    private String managerId;
    @SerializedName("from_date")
    @Expose
    private String fromDate;
    @SerializedName("to_date")
    @Expose
    private String toDate;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("permission_id")
    @Expose
    private String permissionId;
    @SerializedName("picture1")
    @Expose
    private String picture1;
    @SerializedName("picture2")
    @Expose
    private String picture2;
    @SerializedName("picture3")
    @Expose
    private String picture3;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("comment")
    @Expose
    private String comment;

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isParent() {
        return parent;
    }

    public void setParent(boolean parent) {
        this.parent = parent;
    }

    public boolean expanded = false;
    public boolean parent = false;

    public final static Creator<PermissionActivity> CREATOR = new Creator<PermissionActivity>() {


        @SuppressWarnings({
            "unchecked"
        })
        public PermissionActivity createFromParcel(Parcel in) {
            return new PermissionActivity(in);
        }

        public PermissionActivity[] newArray(int size) {
            return (new PermissionActivity[size]);
        }

    }
    ;
    private final static long serialVersionUID = 4384112885641968274L;

    protected PermissionActivity(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.managerId = ((String) in.readValue((String.class.getClassLoader())));
        this.fromDate = ((String) in.readValue((String.class.getClassLoader())));
        this.toDate = ((String) in.readValue((String.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.permissionId = ((String) in.readValue((String.class.getClassLoader())));
        this.picture1 = ((String) in.readValue((String.class.getClassLoader())));
        this.picture2 = ((String) in.readValue((String.class.getClassLoader())));
        this.picture3 = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.comment = ((String) in.readValue((String.class.getClassLoader())));
    }

    public PermissionActivity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public String getPicture1() {
        return picture1;
    }

    public void setPicture1(String picture1) {
        this.picture1 = picture1;
    }

    public String getPicture2() {
        return picture2;
    }

    public void setPicture2(String picture2) {
        this.picture2 = picture2;
    }

    public String getPicture3() {
        return picture3;
    }

    public void setPicture3(String picture3) {
        this.picture3 = picture3;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(managerId);
        dest.writeValue(fromDate);
        dest.writeValue(toDate);
        dest.writeValue(userId);
        dest.writeValue(permissionId);
        dest.writeValue(picture1);
        dest.writeValue(picture2);
        dest.writeValue(picture3);
        dest.writeValue(status);
        dest.writeValue(comment);
    }

    public int describeContents() {
        return  0;
    }

}
