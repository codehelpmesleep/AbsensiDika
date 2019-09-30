
package com.juaracoding.absensidika.Login.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("pass")
    @Expose
    private String pass;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("banned")
    @Expose
    private String banned;
    @SerializedName("last_login")
    @Expose
    private String lastLogin;
    @SerializedName("last_activity")
    @Expose
    private String lastActivity;
    @SerializedName("date_created")
    @Expose
    private String dateCreated;
    @SerializedName("forgot_exp")
    @Expose
    private Object forgotExp;
    @SerializedName("remember_time")
    @Expose
    private String rememberTime;
    @SerializedName("remember_exp")
    @Expose
    private String rememberExp;
    @SerializedName("verification_code")
    @Expose
    private Object verificationCode;
    @SerializedName("top_secret")
    @Expose
    private Object topSecret;
    @SerializedName("ip_address")
    @Expose
    private String ipAddress;
    public final static Parcelable.Creator<Data> CREATOR = new Creator<Data>() {


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
    private final static long serialVersionUID = -1844366545395643076L;

    protected Data(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.pass = ((String) in.readValue((String.class.getClassLoader())));
        this.username = ((String) in.readValue((String.class.getClassLoader())));
        this.fullName = ((String) in.readValue((String.class.getClassLoader())));
        this.avatar = ((String) in.readValue((String.class.getClassLoader())));
        this.banned = ((String) in.readValue((String.class.getClassLoader())));
        this.lastLogin = ((String) in.readValue((String.class.getClassLoader())));
        this.lastActivity = ((String) in.readValue((String.class.getClassLoader())));
        this.dateCreated = ((String) in.readValue((String.class.getClassLoader())));
        this.forgotExp = ((Object) in.readValue((Object.class.getClassLoader())));
        this.rememberTime = ((String) in.readValue((String.class.getClassLoader())));
        this.rememberExp = ((String) in.readValue((String.class.getClassLoader())));
        this.verificationCode = ((Object) in.readValue((Object.class.getClassLoader())));
        this.topSecret = ((Object) in.readValue((Object.class.getClassLoader())));
        this.ipAddress = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Data() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBanned() {
        return banned;
    }

    public void setBanned(String banned) {
        this.banned = banned;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(String lastActivity) {
        this.lastActivity = lastActivity;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Object getForgotExp() {
        return forgotExp;
    }

    public void setForgotExp(Object forgotExp) {
        this.forgotExp = forgotExp;
    }

    public String getRememberTime() {
        return rememberTime;
    }

    public void setRememberTime(String rememberTime) {
        this.rememberTime = rememberTime;
    }

    public String getRememberExp() {
        return rememberExp;
    }

    public void setRememberExp(String rememberExp) {
        this.rememberExp = rememberExp;
    }

    public Object getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(Object verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Object getTopSecret() {
        return topSecret;
    }

    public void setTopSecret(Object topSecret) {
        this.topSecret = topSecret;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(email);
        dest.writeValue(pass);
        dest.writeValue(username);
        dest.writeValue(fullName);
        dest.writeValue(avatar);
        dest.writeValue(banned);
        dest.writeValue(lastLogin);
        dest.writeValue(lastActivity);
        dest.writeValue(dateCreated);
        dest.writeValue(forgotExp);
        dest.writeValue(rememberTime);
        dest.writeValue(rememberExp);
        dest.writeValue(verificationCode);
        dest.writeValue(topSecret);
        dest.writeValue(ipAddress);
    }

    public int describeContents() {
        return  0;
    }

}
