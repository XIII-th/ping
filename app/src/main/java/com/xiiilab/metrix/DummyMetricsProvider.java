package com.xiiilab.metrix;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.NonNull;
import android.util.Log;
import com.xiiilab.metrix.persistance.MetricEntity;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Sergey on 18.07.2018
 */
public class DummyMetricsProvider implements LifecycleObserver {

    private static final byte METRICS_COUNT = 10;
    private static final long REFRESH_FREQUENCY = 5000L;
    private final ThreadGroup mThreadGroup;
    private final ExecutorService mExecutorService;
    private final Random mRandom;
    private final Repository mRepository;
    private boolean mEnabled;

    public DummyMetricsProvider(Repository repository) {
        mRepository = repository;
        mThreadGroup = new ThreadGroup("DUMMY_METRICS_THREAD_GROUP");
        mExecutorService = Executors.newFixedThreadPool(4, this::createThread);
        mRandom = new Random();
    }

    public void enable() {
        mEnabled = true;
    }

    public void disable() {
        mEnabled = false;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void startUpdate() {
        if (mEnabled)
            mExecutorService.submit(this::generateResponses);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void stopUpdate() {
        mExecutorService.shutdown();
    }

    private void generateResponses() {
        while (!mExecutorService.isShutdown()) {
            for (int id = 0; id < METRICS_COUNT; id++) {
                MetricEntity entity = mRepository.get(id).getValue();
                if (entity == null)
                    entity = new MetricEntity(id, "Entity " + id, 0);
                RequestSimulator simulator = new RequestSimulator(mRandom, mRepository, entity);
                mExecutorService.submit(simulator);
            }

            try {
                Thread.sleep(REFRESH_FREQUENCY);
            } catch (InterruptedException e) {
                Log.e(getClass().getName(), "Unable to sleep in response generator", e);
            }
        }
    }

    private Thread createThread(@NonNull Runnable runnable) {
        Thread thread = new Thread(mThreadGroup, runnable);
        thread.setPriority(Thread.MIN_PRIORITY);
        return thread;
    }

    private static class RequestSimulator implements Runnable {

        private final Random mRandom;
        private final Repository mRepository;
        private final MetricEntity mMetricEntity;

        private RequestSimulator(Random random, Repository repository, MetricEntity metricEntity) {
            mRandom = random;
            mRepository = repository;
            mMetricEntity = metricEntity;
        }

        @Override
        public void run() {
            try {
                int latency = mRandom.nextInt(3000);
                Thread.sleep(latency);
                mMetricEntity.setCounter(latency);
                mRepository.insert(mMetricEntity);
            } catch (InterruptedException e) {
                Log.e(getClass().getName(), "Unable to sleep request thread", e);
            }
        }
    }
}
