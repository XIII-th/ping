package com.xiiilab.ping;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import com.xiiilab.ping.persistance.HostEntity;
import com.xiiilab.ping.ping.PingRequestExecutor;
import com.xiiilab.ping.repository.Repository;

/**
 * Created by XIII-th on 19.07.2018
 */
public class AppInitializer extends Application {

    private static final String FIRST_START_PREFERENCE_KEY = "com.xiiilab.ping.AppInitializer.FIRST_START_PREFERENCE_KEY";

    @Override
    public void onCreate() {
        super.onCreate();
        Repository.init(this);
        PingRequestExecutor.init(Repository.getInstance());

        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        if (preferences.getBoolean(FIRST_START_PREFERENCE_KEY, true)) {
            new DbInitializer(Repository.getInstance()).execute();
            preferences.edit().putBoolean(FIRST_START_PREFERENCE_KEY, false).apply();
        }
    }

    private static class DbInitializer extends AsyncTask<Void, Void, Void> {

        private static final String TAG = "DB_INITIALIZER";
        private final Repository mRepository;

        private DbInitializer(Repository repository) {
            mRepository = repository;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            long intrigue = 500L;
            try {
                Thread.sleep(intrigue);
                addHost("127.0.0.1", "Localhost");
                Thread.sleep(2L * intrigue);
                addHost("8.8.8.8", "Google DNS");
                Thread.sleep(3L * intrigue);
                addHost("192.30.253.112", "Github");
            } catch (InterruptedException e) {
                Log.d(TAG, "Db initialisation was interrupted", e);
            }
            return null;
        }

        private void addHost(String host, String title) {
            HostEntity entity = new HostEntity(host, title);
            entity.setCreated(System.currentTimeMillis());
            mRepository.insert(entity);
        }
    }
}
