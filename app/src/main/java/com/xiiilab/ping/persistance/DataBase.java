package com.xiiilab.ping.persistance;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by XIII-th on 18.07.2018
 */
@Database(entities = HostEntity.class, version = 1)
public abstract class DataBase extends RoomDatabase {

    public abstract HostDao getHostDao();
}
