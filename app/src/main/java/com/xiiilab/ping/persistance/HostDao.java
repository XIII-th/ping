package com.xiiilab.ping.persistance;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.*;

import java.util.List;

/**
 * Created by XIII-th on 18.07.2018
 * TODO: Avoid false positive notifications for observable queries
 * https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1
 */
@Dao
public interface HostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HostEntity entity);

    @Delete
    void delete(HostEntity entity);

    @Query("SELECT * FROM hosts WHERE host = :host")
    LiveData<HostEntity> get(String host);

    @Query("SELECT * FROM hosts WHERE host = :host")
    HostEntity getSync(String host);

    @Query("SELECT host FROM hosts")
    LiveData<List<String>> hostList();
}
