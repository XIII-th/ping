package com.xiiilab.metrix.persistance;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Sergey on 18.07.2018
 */
public class DummyDataBase {

    private static final DummyDataBase INSTANCE = new DummyDataBase();

    private final MetricEntity[] mData;
    private final MutableLiveData<MetricEntity>[] mDataLiveData;
    private final MutableLiveData<Integer> mCountLiveData;

    private final ExecutorService mExecutorService;

    @SuppressWarnings("unchecked")
    private DummyDataBase() {
        mData = new MetricEntity[30];
        for (int i = 0; i < mData.length; i++) {
            MetricEntity entity = new MetricEntity();
            entity.setId(i);
            entity.setTitle("Entity " + i);
            mData[i] = entity;
        }

        mDataLiveData = new MutableLiveData[mData.length];
        mCountLiveData = new MutableLiveData<>();

        mExecutorService = Executors.newFixedThreadPool(3);
    }

    public static DummyDataBase instance() {
        return INSTANCE;
    }

    public LiveData<MetricEntity> get(int id) {
        MutableLiveData<MetricEntity> liveData = mDataLiveData[id];
        if (liveData == null) {
            liveData = mDataLiveData[id] = new MutableLiveData<>() ;
            mExecutorService.execute(() -> {
                try {
                    long time = new Random().nextInt(1000);
                    Thread.sleep(time);
                    mDataLiveData[id].postValue(mData[id]);
                    Log.d(getClass().getName(), "get: " + id);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        return liveData;
    }

    public LiveData<Integer> getCount() {
        mExecutorService.execute(() -> {
            try {
                Thread.sleep(100L);
                mCountLiveData.postValue(mData.length);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        return mCountLiveData;
    }
}
