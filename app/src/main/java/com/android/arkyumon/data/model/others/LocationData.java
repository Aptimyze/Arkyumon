package com.android.arkyumon.data.model.others;

import android.location.Location;

import androidx.annotation.NonNull;

public class LocationData {

    private Location location;
    private Double acceleration;

    public LocationData(Location location, Double acceleration) {
        this.location = location;
        this.acceleration = acceleration;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();

    }

}
