package com.xiiilab.ping.repository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by XIII-th on 22.07.2018
 */
class TxExecutor {

    private final ThreadGroup mThreadGroup;
    private final ExecutorService mExecutorService;

    public TxExecutor() {
        mThreadGroup = new ThreadGroup("TX_THREAD_GROUP");
        mExecutorService = Executors.newCachedThreadPool(this::createThread);
    }

    public void execute(Runnable action) {
        // TODO: 22.07.2018 notification of request result
        mExecutorService.execute(action);
    }

    private Thread createThread(Runnable task) {
        Thread thread = new Thread(mThreadGroup, task);
        thread.setPriority(Thread.MIN_PRIORITY);
        return thread;
    }
}
