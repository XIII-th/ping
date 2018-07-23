package com.xiiilab.ping.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Patterns;
import com.xiiilab.ping.R;
import com.xiiilab.ping.persistance.HostEntity;
import com.xiiilab.ping.repository.Repository;

/**
 * Created by Sergey on 20.07.2018
 */
public class EditViewModel extends AndroidViewModel {

    private final MutableLiveData<String> mErrorIp = new MutableLiveData<>();
    private final MutableLiveData<String> mErrorFrequency = new MutableLiveData<>();
    private final MutableLiveData<String> mErrorTimeout = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNewEntity = new MutableLiveData<>();
    private final MediatorLiveData<HostEntity> mEntity;
    private Repository mRepository;

    public EditViewModel(@NonNull Application application) {
        super(application);
        mEntity = new MediatorLiveData<>();
    }

    public void setRepository(Repository repository) {
        mRepository = repository;
    }

    public void load(@Nullable String hostId) {
        if (hostId == null)
            setupEntity(null);
        else if (mRepository == null)
            throw new IllegalStateException("Unable to load data from null repository");
        else
            mEntity.addSource(mRepository.getAsync(hostId), this::setupEntity);
    }

    public boolean isInitialised() {
        return mEntity.getValue() != null;
    }

    public LiveData<HostEntity> getEntity() {
        return mEntity;
    }

    public LiveData<Boolean> isNewEntity() {
        return mNewEntity;
    }

    public LiveData<String> getIpError() {
        return mErrorIp;
    }

    public LiveData<String> getFrequencyError() {
        return mErrorFrequency;
    }

    public LiveData<String> getTimeoutError() {
        return mErrorTimeout;
    }

    public boolean save() {
        HostEntity entity = mEntity.getValue();
        if (entity == null)
            // TODO: 22.07.2018 implement more robust solution for "loading delay" situation
            throw new IllegalStateException("Entity is not loaded yet");

        if (!Patterns.IP_ADDRESS.matcher(entity.getHost()).find())
            mErrorIp.setValue(getApplication().getString(R.string.wrong_ip_address));
        else
            mErrorIp.setValue(null);

        if (entity.getFrequency() <= 0)
            mErrorFrequency.setValue(getApplication().getString(R.string.frequency_must_be_positive));
        else
            mErrorFrequency.setValue(null);

        if (entity.getTimeout() <= 0)
            mErrorTimeout.setValue(getApplication().getString(R.string.timeout_must_be_positive));
        else
            mErrorTimeout.setValue(null);

        boolean noError =
                mErrorIp.getValue() == null &&
                mErrorFrequency.getValue() == null &&
                mErrorTimeout.getValue() == null;

        if (noError)
            mRepository.insert(entity);

        return noError;
    }

    private void setupEntity(HostEntity entity) {
        if (entity == null) {
            mNewEntity.setValue(Boolean.TRUE);
            mEntity.setValue(new HostEntity(""));
        } else {
            mNewEntity.setValue(Boolean.FALSE);
            mEntity.setValue(entity);
        }
    }
}
