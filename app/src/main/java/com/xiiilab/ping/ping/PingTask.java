package com.xiiilab.ping.ping;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;
import com.xiiilab.ping.persistance.HostEntity;
import com.xiiilab.ping.repository.Repository;

/**
 * Created by Sergey on 22.07.2018
 */
class PingTask implements Runnable {

    private static final String TAG = "PING_TASK";

    private final Repository mRepository;
    private final String mHost;
    private final MutableLiveData<Integer> mValue;
    private volatile HostEntity mEntity;
    private volatile boolean mInitialisationCompleted;
    private volatile boolean mWork;

    public PingTask(Repository repository, String host, MutableLiveData<Integer> ping) {
        mRepository = repository;
        mHost = host;
        mValue = ping;
        mWork = true;
    }

    public MutableLiveData<Integer> getValue() {
        return mValue;
    }

    public HostEntity getEntity() {
        synchronized (this) {
            if (!mInitialisationCompleted)
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    Log.d(TAG, "Unable to wait task initialisation", e);
                }
        }
        return mEntity;
    }

    @Override
    public void run() {
        mEntity = mRepository.getSync(mHost);

        synchronized (this) {
            mInitialisationCompleted = true;
            this.notifyAll();
        }

        Log.d(TAG, "Starting ping loop for host " + mEntity);
        mWork = mEntity != null;
        int counter = 0;
        while (mWork) {
            // TODO: 22.07.2018 make request
            mValue.postValue(counter++);
            Log.d(TAG, mEntity + " " + counter);
            try {
                Thread.sleep(mEntity.getFrequency());
            } catch (InterruptedException e) {
                Log.d(TAG, "Request for host '" + mEntity + "' was interrupted");
                mWork = false;
            }
        }
        Log.d(TAG, "Ping loop for host " + mEntity + " was stopped");
        // reset state
        mWork = true;
    }

    public void stop() {
        Log.d(TAG, "Request for stop of host " + mEntity);
        mWork = false;
    }

}
