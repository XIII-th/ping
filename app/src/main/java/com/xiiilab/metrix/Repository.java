package com.xiiilab.metrix;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import com.xiiilab.metrix.persistance.DataBase;
import com.xiiilab.metrix.persistance.MetricEntity;

/**
 * Created by Sergey on 18.07.2018
 */
public class Repository {

    private static Repository INSTANCE;

    private final DataBase mDataBase;

    private Repository(DataBase db) {
        mDataBase = db;
    }

    public static Repository getInstance() {
        if (INSTANCE == null)
            throw new IllegalStateException("Repository is not initialised");
        return INSTANCE;
    }

    public static void init(Context context) {
        DataBase db = Room.databaseBuilder(context, DataBase.class, "metrix.db").build();
        INSTANCE = new Repository(db);
    }

    public LiveData<MetricEntity> get(int id) {
        return mDataBase.getMetricsDao().get(id);
    }

    public LiveData<Integer> getCount() {
        return mDataBase.getMetricsDao().getCount();
    }

    public void insert(MetricEntity entity) {
        mDataBase.getMetricsDao().insert(entity);
    }
}
