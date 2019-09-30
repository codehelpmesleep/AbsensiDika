
package com.juaracoding.absensidika.Login.model;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelLogin implements Serializable, Parcelable
{

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("groups")
    @Expose
    private List<String> groups = null;
    public final static Parcelable.Creator<ModelLogin> CREATOR = new Creator<ModelLogin>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ModelLogin createFromParcel(Parcel in) {
            return new ModelLogin(in);
        }

        public ModelLogin[] newArray(int size) {
            return (new ModelLogin[size]);
        }

    }
            ;
    private final static long serialVersionUID = -3615386818799135257L;

    protected ModelLogin(Parcel in) {
        this.status = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.data = ((Data) in.readValue((Data.class.getClassLoader())));
        this.token = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.groups, (java.lang.String.class.getClassLoader()));
    }

    public ModelLogin() {
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(message);
        dest.writeValue(data);
        dest.writeValue(token);
        dest.writeList(groups);
    }

    public int describeContents() {
        return  0;
    }

}
