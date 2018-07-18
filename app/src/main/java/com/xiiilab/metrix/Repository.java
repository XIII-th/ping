package com.xiiilab.metrix;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.persistence.room.Room;
import android.content.Context;
import com.xiiilab.metrix.persistance.DataBase;
import com.xiiilab.metrix.persistance.MetricEntity;

/**
 * Created by Sergey on 18.07.2018
 */
public class Repository implements LifecycleObserver {

    private DataBase mDataBase;

    public Repository(Context context) {
        mDataBase = Room.databaseBuilder(context, DataBase.class, "metrix.db").build();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void close() {
        mDataBase.close();
        mDataBase = null;
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
