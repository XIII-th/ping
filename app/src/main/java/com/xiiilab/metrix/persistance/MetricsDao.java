package com.xiiilab.metrix.persistance;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.*;

/**
 * Created by Sergey on 18.07.2018
 */
@Dao
public interface MetricsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MetricEntity entity);

    @Delete
    void delete(MetricEntity entity);

    @Query("SELECT * FROM metric WHERE id = :id")
    LiveData<MetricEntity> get(int id);

    @Query("SELECT Count(*) FROM metric")
    LiveData<Integer> getCount();
}
