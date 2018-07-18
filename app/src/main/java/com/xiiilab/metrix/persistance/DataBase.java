package com.xiiilab.metrix.persistance;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by Sergey on 18.07.2018
 */
@Database(entities = MetricEntity.class, version = 1)
public abstract class DataBase extends RoomDatabase {

    public abstract MetricsDao getMetricsDao();
}
