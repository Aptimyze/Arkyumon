package com.android.arkyumon.data.model.db;

import android.location.Location;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "potholes")
public class Potholes {

    @PrimaryKey
    private Location location;

    private double acceleration;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

}
