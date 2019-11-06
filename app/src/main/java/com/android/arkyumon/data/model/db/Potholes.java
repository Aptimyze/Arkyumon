package com.android.arkyumon.data.model.db;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "potholes")
public class Potholes {

    @Override
    public String toString() {
        return "Potholes{" +
                "timestamp='" + timestamp + '\'' +
                ", latitude=" + latitude +
                ", absoluteDifference=" + absoluteDifference +
                ", longitude=" + longitude +
                ", acceleration=" + acceleration +
                '}';
    }

    public Potholes(@NonNull String timestamp, double latitude, double absoluteDifference, double longitude, double acceleration) {
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.absoluteDifference = absoluteDifference;
        this.longitude = longitude;
        this.acceleration = acceleration;
    }

    @NonNull
    @PrimaryKey
    private String timestamp;

    private double latitude;

    private double absoluteDifference;

    private double longitude;

    private double acceleration;

    public Potholes() {

    }

    public double getAbsoluteDifference() {
        return absoluteDifference;
    }

    public void setAbsoluteDifference(double absoluteDifference) {
        this.absoluteDifference = absoluteDifference;
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

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    @NonNull
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NonNull String timestamp) {
        this.timestamp = timestamp;
    }


}
