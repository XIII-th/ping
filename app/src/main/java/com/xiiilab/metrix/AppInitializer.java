package com.xiiilab.metrix;

import android.app.Application;

/**
 * Created by Sergey on 19.07.2018
 */
public class AppInitializer extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Repository.init(this);
        DummyMetricsProvider.init(Repository.getInstance());
    }
}
