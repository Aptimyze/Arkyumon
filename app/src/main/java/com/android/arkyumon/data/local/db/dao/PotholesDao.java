package com.android.arkyumon.data.local.db.dao;

import android.location.Location;

import androidx.room.Dao;
import androidx.room.Insert;

import com.android.arkyumon.data.model.db.Potholes;

@Dao
public interface PotholesDao {

    @Insert
    public void addPothole(Potholes potholes);
}
