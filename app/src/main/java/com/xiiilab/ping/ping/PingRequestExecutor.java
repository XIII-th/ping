package com.xiiilab.ping.ping;

import android.arch.lifecycle.*;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.xiiilab.ping.DiffList;
import com.xiiilab.ping.repository.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Sergey on 18.07.2018
 */
public class PingRequestExecutor implements LifecycleObserver {

    private static PingRequestExecutor mInstance;

    private final ThreadGroup mThreadGroup;
    private final ExecutorService mRequestExecutor;
    private final ExecutorService mRefreshExecutor;

    private final Repository mRepository;

    private final DiffList<String> mHosts;
    private final HashMap<String, PingTask> mActivePing;
    private final HashMap<String, MutableLiveData<Integer>> mPingValue;

    private PingRequestExecutor(Repository repository) {
        mRepository = repository;
        mThreadGroup = new ThreadGroup("HOST_PING_THREAD_GROUP");
        mRequestExecutor = Executors.newCachedThreadPool(this::createPingThread);
        mRefreshExecutor = Executors.newSingleThreadExecutor(this::createRefreshThread);

        mHosts = new DiffList<>();
        mActivePing = new HashMap<>();
        mPingValue = new HashMap<>();
    }

    public static void init(Repository repository) {
        if (mInstance != null)
            throw new IllegalStateException("Host provider already initialised");
        mInstance = new PingRequestExecutor(repository);
        // important to register owner first while request list is empty
        ProcessLifecycleOwner.get().getLifecycle().addObserver(mInstance);
        // first time this observer populate and start requests
        repository.hostList().observe(ProcessLifecycleOwner.get(), mInstance::refresh);
    }

    public static PingRequestExecutor getInstance() {
        if (mInstance == null)
            throw new IllegalStateException("Host provider is not initialised");
        return mInstance;
    }

    @Nullable
    public LiveData<Integer> getPingValue(@NonNull String host) {
        synchronized (mPingValue) {
            return mPingValue.get(host);
        }
    }

    public void setTrackedHost(String host) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void resetTracking() {
        setTrackedHost(null);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void enable() {
        synchronized (mActivePing) {
            for (PingTask request : mActivePing.values())
                mRequestExecutor.execute(request);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void disable() {
        synchronized (mActivePing) {
            for (PingTask request : mActivePing.values())
                request.stop();
        }
    }

    private synchronized void refresh(List<String> hosts) {
        mHosts.
                beginMigration(hosts).
                onInsert(this::startNewPingTask).
                onRemove(this::removePingTask).
                completeMigration();
        mRefreshExecutor.execute(new RefreshTask(mRepository, mActivePing, mRequestExecutor));
    }

    private void startNewPingTask(int ignored, String host) {
        PingTask request;
        synchronized (mActivePing) {
            if (mActivePing.containsKey(host))
                throw new IllegalStateException("Ping task for host '" + host + "' already present");
            MutableLiveData<Integer> pingValue = new MutableLiveData<>();

            synchronized (mPingValue) {
                mPingValue.put(host, pingValue);
            }

            request = new PingTask(mRepository, host, pingValue);
            mActivePing.put(host, request);
        }
        mRequestExecutor.execute(request);
    }

    private void removePingTask(int ignored, String host) {
        synchronized (mPingValue) {
            mPingValue.remove(host);
        }

        synchronized (mActivePing) {
            PingTask request = mActivePing.remove(host);
            if (request != null)
                request.stop();
        }
    }

    private Thread createPingThread(@NonNull Runnable runnable) {
        Thread thread = new Thread(mThreadGroup, runnable);
        thread.setName("PING_THREAD");
        thread.setPriority(Thread.MIN_PRIORITY);
        return thread;
    }

    private Thread createRefreshThread(Runnable runnable) {
        Thread thread = createPingThread(runnable);
        thread.setName("PING_REFRESH_THREAD");
        return thread;
    }
}