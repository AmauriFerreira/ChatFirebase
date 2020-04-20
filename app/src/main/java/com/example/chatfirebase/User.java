package com.example.chatfirebase;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String uuid;
    private  String usernome;
    private  String profileUrl;
    private String token;
    private boolean online;

public  User(){

}

    public User(String uuid , String usernome, String profileUrl) {

        this.uuid = uuid;
        this.usernome = usernome;
        this.profileUrl = profileUrl;
    }

    protected User(Parcel in) {
        uuid = in.readString();
        usernome = in.readString();
        profileUrl = in.readString();
        token = in.readString();
        online = in.readInt() == 1;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUuid() {
        return uuid;
    }

    public String getUsernome() {
        return usernome;
    }

    public String getProfileUrl() {
        return profileUrl;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uuid);
        dest.writeString(usernome);
        dest.writeString(profileUrl);
        dest.writeString(token);
        dest.writeInt(online ? 1 : 0);
    }
}
