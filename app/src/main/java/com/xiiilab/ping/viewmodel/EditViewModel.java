package com.xiiilab.ping.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;
import com.xiiilab.ping.persistance.HostEntity;

/**
 * Created by Sergey on 20.07.2018
 */
public class EditViewModel extends ViewModel {

    private final MutableLiveData<String> mErrorString = new MutableLiveData<>();
    private HostEntity mEntity;

    public void load(@Nullable String hostId) {
        if (hostId == null)
            mEntity = new HostEntity("");
    }

    public boolean isInitialised() {
        return mEntity != null;
    }

    public LiveData<String> getIpErrorString() {
        return mErrorString;
    }

    public String getIp() {
        return mEntity.getHost();
    }

    public void setIp(String ip) {
        mEntity.setHost(ip);
    }

    public String getTitle() {
        return mEntity.getTitle();
    }

    public void setTitle(String title) {
        mEntity.setTitle(title);
    }

    public int getTimeout() {
        return mEntity.getTimeout();
    }

    public void setTimeout(int timeout) {
        mEntity.setTimeout(timeout);
    }

    public boolean save() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
