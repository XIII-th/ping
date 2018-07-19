package com.xiiilab.metrix;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ProcessLifecycleOwner;
import android.support.annotation.NonNull;
import android.util.Log;
import com.xiiilab.metrix.persistance.MetricEntity;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Sergey on 18.07.2018
 */
public class DummyMetricsProvider implements LifecycleObserver {

    private static final byte METRICS_COUNT = 10;
    private static final long REFRESH_FREQUENCY = 3000L;
    private static final int LATENCY_LIMIT = 1000;

    private static DummyMetricsProvider mInstance;

    private final ExecutorService mRequestGeneratorExecutor;
    private final ThreadGroup mThreadGroup;
    private final ExecutorService mRequestExecutor;
    private final Random mRandom;
    private final Repository mRepository;
    private volatile Future<?> mActiveGenerator;

    private final Object mMetricIdLock;
    private volatile Integer mTrackedMetricId;

    private DummyMetricsProvider(Repository repository) {
        mRepository = repository;
        mThreadGroup = new ThreadGroup("DUMMY_METRICS_THREAD_GROUP");
        mRequestGeneratorExecutor = Executors.newSingleThreadExecutor();
        int poolSize = Runtime.getRuntime().availableProcessors();
        if (poolSize > 1)
            // one thread for request generator
            poolSize--;
        mRequestExecutor = Executors.newFixedThreadPool(poolSize, this::createThread);
        mRandom = new Random();
        mMetricIdLock = new Object();
    }

    public static void init(Repository repository) {
        if (mInstance != null)
            throw new IllegalStateException("Metrics provider already initialised");
        mInstance = new DummyMetricsProvider(repository);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(mInstance);
    }

    public static DummyMetricsProvider getInstance() {
        if (mInstance == null)
            throw new IllegalStateException("Metrics provider is not initialised");
        return mInstance;
    }

    public void setTrackedMetric(Integer id) {
        synchronized (mMetricIdLock) {
            mTrackedMetricId = id;
        }
    }

    public void resetTracking() {
        setTrackedMetric(null);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void enable() {
        if (mActiveGenerator == null || mActiveGenerator.isCancelled())
            mActiveGenerator = mRequestGeneratorExecutor.submit(this::generateResponses);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void disable() {
        if (mActiveGenerator != null) {
            mActiveGenerator.cancel(true);
            mActiveGenerator = null;
        }
    }

    private void generateResponses() {
        Log.d(getClass().getName(), "Request generator started");
        Thread currentThread = Thread.currentThread();
        while (!currentThread.isInterrupted()) {
            if (!trackSingle())
                for (int id = 0; id < METRICS_COUNT; id++) {
                    if (currentThread.isInterrupted())
                        break;
                    trackMetric(id);
                }

            try {
                Thread.sleep(REFRESH_FREQUENCY);
            } catch (InterruptedException e) {
                Log.d(getClass().getName(), "Thread interrupted while sleep");
            }
        }
        Log.d(getClass().getName(), "Request generator stopped");
    }

    private boolean trackSingle() {
        synchronized (mMetricIdLock) {
            if (mTrackedMetricId != null) {
                trackMetric(mTrackedMetricId);
                return true;
            }
            return false;
        }
    }

    private void trackMetric(int id) {
        MetricEntity entity = mRepository.get(id).getValue();
        if (entity == null)
            entity = new MetricEntity(id, "Entity " + id, 0);
        RequestSimulator simulator = new RequestSimulator(mRandom, mRepository, entity);
        mRequestExecutor.execute(simulator);
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
                int latency = mRandom.nextInt(LATENCY_LIMIT);
                Thread.sleep(latency);
                mMetricEntity.setCounter(latency);
                mRepository.insert(mMetricEntity);
            } catch (InterruptedException e) {
                Log.e(getClass().getName(), "Unable to sleep request thread", e);
            }
        }
    }
}
