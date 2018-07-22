package com.xiiilab.ping.repository;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import com.xiiilab.ping.persistance.DataBase;
import com.xiiilab.ping.persistance.HostEntity;

import java.util.List;

/**
 * Created by Sergey on 18.07.2018
 */
public class Repository {

    private static Repository mInstance;

    private final TxExecutor mTxExecutor;
    private final DataBase mDataBase;

    private Repository(Context appContext) {
        mTxExecutor = new TxExecutor();
        mDataBase = Room.databaseBuilder(appContext, DataBase.class, "ping.db").build();
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

    public LiveData<HostEntity> get(String host) {
        return mDataBase.getHostDao().get(host);
    }

    public LiveData<List<String>> hostList() {
        return mDataBase.getHostDao().hostList();
    }

    public void insert(HostEntity entity) {
        mTxExecutor.execute(() -> mDataBase.getHostDao().insert(entity));
    }
}
