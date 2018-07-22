package com.xiiilab.ping.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Patterns;
import com.xiiilab.ping.R;
import com.xiiilab.ping.repository.Repository;
import com.xiiilab.ping.persistance.HostEntity;

/**
 * Created by Sergey on 20.07.2018
 */
public class EditViewModel extends AndroidViewModel {

    private final MutableLiveData<String> mErrorIp = new MutableLiveData<>();
    private final MutableLiveData<String> mErrorFrequency = new MutableLiveData<>();
    private final MutableLiveData<String> mErrorTimeout = new MutableLiveData<>();
    private HostEntity mEntity;

    public EditViewModel(@NonNull Application application) {
        super(application);
    }

    public void load(@Nullable String hostId) {
        if (hostId == null)
            mEntity = new HostEntity("");
    }

    public boolean isInitialised() {
        return mEntity != null;
    }

    public String getIp() {
        return mEntity.getHost();
    }

    public void setIp(String ip) {
        mEntity.setHost(ip);
    }

    public LiveData<String> getIpError() {
        return mErrorIp;
    }

    public String getTitle() {
        return mEntity.getTitle();
    }

    public void setTitle(String title) {
        mEntity.setTitle(title);
    }

    public int getFrequency() {
        return mEntity.getFrequency();
    }

    public void setFrequency(int frequency) {
        mEntity.setFrequency(frequency);
    }

    public LiveData<String> getFrequencyError() {
        return mErrorFrequency;
    }

    public int getTimeout() {
        return mEntity.getTimeout();
    }

    public void setTimeout(int timeout) {
        mEntity.setTimeout(timeout);
    }

    public LiveData<String> getTimeoutError() {
        return mErrorTimeout;
    }

    public boolean save() {
        if (!Patterns.IP_ADDRESS.matcher(mEntity.getHost()).find())
            mErrorIp.setValue(getApplication().getString(R.string.wrong_ip_address));
        else
            mErrorIp.setValue(null);

        if (mEntity.getFrequency() <= 0)
            mErrorFrequency.setValue(getApplication().getString(R.string.frequency_must_be_positive));
        else
            mErrorFrequency.setValue(null);

        if (mEntity.getTimeout() <= 0)
            mErrorTimeout.setValue(getApplication().getString(R.string.timeout_must_be_positive));
        else
            mErrorTimeout.setValue(null);

        boolean noError =
                mErrorIp.getValue() == null &&
                mErrorFrequency.getValue() == null &&
                mErrorTimeout.getValue() == null;

        if (noError)
            Repository.getInstance().insert(mEntity);

        return noError;
    }
}
