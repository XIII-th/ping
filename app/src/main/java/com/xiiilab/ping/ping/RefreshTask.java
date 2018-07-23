package com.xiiilab.ping.ping;

import android.util.Log;
import com.xiiilab.ping.persistance.HostEntity;
import com.xiiilab.ping.repository.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Created by Sergey on 23.07.2018
 */
class RefreshTask implements Runnable {

    private static final String TAG = "PING_LIST_REFRESH_TASK";
    private final Repository mRepository;
    private final HashMap<String, PingTask> mActive;
    private final ExecutorService mExecutor;

    public RefreshTask(Repository repository, HashMap<String, PingTask> active, ExecutorService executor) {
        mRepository = repository;
        mActive = active;
        mExecutor = executor;
    }

    @Override
    public void run() {
        synchronized (mActive) {
            Log.d(TAG, "Refresh task started");
            for (Map.Entry<String, PingTask> entry : mActive.entrySet()) {
                PingTask task = entry.getValue();
                HostEntity fresh = mRepository.getSync(entry.getKey());
                if (!fresh.equals(task.getEntity())) {
                    // stop task because of it can have long sleep period or very high ping
                    task.stop();
                    // start new task
                    // TODO: 23.07.2018 pass fresh entity to new task instead of request it inside of task
                    task = new PingTask(mRepository, entry.getKey(), task.getValue());
                    Log.d(TAG, "Starting new ping task for host " + fresh);
                    mActive.put(entry.getKey(), task);
                    mExecutor.submit(task);
                }
            }
            Log.d(TAG, "Refresh task completed");
        }
    }
}
