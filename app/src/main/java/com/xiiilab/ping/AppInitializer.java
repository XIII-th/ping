package com.xiiilab.ping;

import android.app.Application;
import com.xiiilab.ping.ping.PingRequestExecutor;
import com.xiiilab.ping.repository.Repository;

/**
 * Created by Sergey on 19.07.2018
 */
public class AppInitializer extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Repository.init(this);
        PingRequestExecutor.init(Repository.getInstance());
    }
}
