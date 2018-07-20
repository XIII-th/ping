package com.xiiilab.ping.persistance;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.*;

import java.util.List;

/**
 * Created by Sergey on 18.07.2018
 */
@Dao
public interface HostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HostEntity entity);

    @Delete
    void delete(HostEntity entity);

    @Query("SELECT * FROM hosts WHERE host = :host")
    LiveData<HostEntity> get(String host);

    @Query("SELECT host FROM hosts")
    LiveData<List<String>> hostList();
}
