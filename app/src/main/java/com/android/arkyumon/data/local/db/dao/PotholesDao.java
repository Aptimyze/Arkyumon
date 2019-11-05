package com.android.arkyumon.data.local.db.dao;

import android.location.Location;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.android.arkyumon.data.model.db.Potholes;

import java.util.List;

@Dao
public interface PotholesDao {

    @Insert
    public void addPothole(Potholes potholes);

    @Query("select * from potholes")
    public List<Potholes> getPotholes();

    @Delete
    public void deletePotholes(Potholes pothole);
}
