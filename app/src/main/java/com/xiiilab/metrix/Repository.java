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

    private static Repository mInstance;

    private DataBase mDataBase;

    private Repository(Context appContext) {
        mDataBase = Room.databaseBuilder(appContext, DataBase.class, "metrix.db").build();
    }

    public static void init(Context appContext) {
        if (mInstance != null)
            throw new IllegalStateException("Repository already initialised");
        mInstance = new Repository(appContext);

    }

    public static Repository getInstance() {
        if (mInstance == null)
            throw new IllegalStateException("repository is not initialised");
        return mInstance;
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
