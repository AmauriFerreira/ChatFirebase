package com.example.chatfirebase;

import android.os.Parcel;
import android.os.Parcelable;

public class MessageSala extends Message {


    private String nomesala;
    private String token;
    private String uuid;


    public MessageSala(){
    }

    public MessageSala(String uuid , String nomesala) {
        this.uuid = uuid;
        this.nomesala = nomesala;
        setToken(uuid);
    }

    public MessageSala(Parcel in) {
        uuid = in.readString();
        nomesala = in.readString();
        token = in.readString();
    }

    public static final Parcelable.Creator<Salas> CREATOR = new Parcelable.Creator<Salas>() {
        @Override
        public Salas createFromParcel(Parcel in) {
            return new Salas(in);
        }

        @Override
        public Salas[] newArray(int size) {
            return new Salas[size];
        }
    };
    public String getNomesala() {
        return nomesala;
    }

    public void setNomesala(String nomesala) {
        this.nomesala = nomesala;
    }
    public String getUuid() {
        return uuid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uuid);
        dest.writeString(nomesala);
        dest.writeString(token);

    }



}

