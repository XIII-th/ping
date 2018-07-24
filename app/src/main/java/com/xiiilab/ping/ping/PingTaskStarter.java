package com.xiiilab.ping.ping;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;
import com.stealthcopter.networktools.Ping;
import com.stealthcopter.networktools.ping.PingResult;
import com.stealthcopter.networktools.ping.PingStats;
import com.xiiilab.ping.persistance.HostEntity;
import com.xiiilab.ping.repository.Repository;

/**
 * Created by Sergey on 22.07.2018
 */
class PingTaskStarter implements Runnable, Ping.PingListener {

    private static final String TAG = "PING_TASK_STARTER";

    private final Repository mRepository;
    private final String mHost;
    private final MutableLiveData<PingResult> mValue;
    private Ping mPing;
    private volatile HostEntity mEntity;
    private volatile boolean mInitialisationCompleted;

    public PingTaskStarter(Repository repository, String host, MutableLiveData<PingResult> ping) {
        mRepository = repository;
        mHost = host;
        mValue = ping;
    }

    public MutableLiveData<PingResult> getValue() {
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
        if (mEntity != null){
            mPing = Ping.
                    onAddress(mEntity.getHost()).
                    setDelayMillis(mEntity.getFrequency()).
                    setTimes(mEntity.getTimeout()).
                    doPing(this);
        }
        Log.d(TAG, "Ping loop for host " + mEntity + " successfully started");
    }

    public void stop() {
        Log.d(TAG, "Request for stop of host " + mEntity);
        mPing.cancel();
    }

    @Override
    public void onResult(PingResult pingResult) {
        mValue.postValue(pingResult);
    }

    @Override
    public void onFinished(PingStats pingStats) {
        Log.d(TAG, "Ping thread successfully stopped");
    }

    @Override
    public void onError(Exception e) {
        PingResult result = new PingResult(null);
        result.isReachable = false;
        result.error = e.getLocalizedMessage();
        mValue.postValue(result);
        Log.d(TAG, "Exception occurred in ping task", e);
    }
}
