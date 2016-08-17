package com.esotericcoder.findmycar.main;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {

    long datetime;
    double latitude;
    double longitude;
    String email;

    public Item(long datetime, double latitude, double longitude, String email) {
        this.datetime = datetime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.email = email;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.datetime);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.email);
    }

    protected Item(Parcel in) {
        this.datetime = in.readLong();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.email = in.readString();
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {return new Item(source);}

        @Override
        public Item[] newArray(int size) {return new Item[size];}
    };
}