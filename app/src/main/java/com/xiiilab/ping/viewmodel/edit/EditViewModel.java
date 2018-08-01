package com.xiiilab.ping.viewmodel.edit;

import android.app.Application;
import android.arch.lifecycle.*;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Patterns;
import com.xiiilab.ping.R;
import com.xiiilab.ping.persistance.HostEntity;
import com.xiiilab.ping.repository.Repository;

/**
 * Created by XIII-th on 20.07.2018
 */
public class EditViewModel extends AndroidViewModel {

    private final MutableLiveData<String> mErrorIp = new MutableLiveData<>();
    private final MutableLiveData<String> mErrorFrequency = new MutableLiveData<>();
    private final MutableLiveData<String> mErrorTimeout = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNewEntity = new MutableLiveData<>();
    private final MediatorLiveData<HostEntity> mEntity;
    private SaveTask mSaveTask;
    private Repository mRepository;

    @SuppressWarnings("unchecked")
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
            mEntity.addSource(mRepository.get(hostId), this::setupEntity);
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

    @MainThread
    public LiveData<Boolean> save(LifecycleOwner owner) {
        // TODO: 31.07.2018 apply best practices. This solution looks weird
        cancelSave();
        mSaveTask = new SaveTask(mRepository, this::checkIp, this::checkFrequency, this::checkTimeout);
        // add observer for delivering entity to save task
        Observer<HostEntity> entityObserver = mSaveTask::startWith;
        mEntity.observe(owner, entityObserver);
        // get result provider
        LiveData<Boolean> result = mSaveTask.getSaveResult();
        // remove save task observer after task complete
        // TODO: 01.08.2018 observer will not be removed if task will be cancelled
        result.observe(owner, ignored -> mEntity.removeObserver(entityObserver));
        return mSaveTask.getSaveResult();
    }

    private void setupEntity(HostEntity entity) {
        if (entity == null) {
            mNewEntity.setValue(Boolean.TRUE);
            HostEntity newEntity = new HostEntity("");
            newEntity.setCreated(System.currentTimeMillis());
            mEntity.setValue(newEntity);
        } else {
            mNewEntity.setValue(Boolean.FALSE);
            mEntity.setValue(entity);
        }
    }

    @Override
    protected void onCleared() {
        cancelSave();
    }

    private void cancelSave() {
        if (mSaveTask != null) {
            mSaveTask.cancel(true);
            mSaveTask = null;
        }
    }

    // --- Save check list will be executed on background thread -------------------------------------------------------

    private boolean checkIp(HostEntity entity) {
        // TODO: 31.07.2018 fix false accept on ip like 127.0.0.999
        String error = null;
        if (!Patterns.IP_ADDRESS.matcher(entity.getHost()).find())
            error = getApplication().getString(R.string.wrong_ip_address);
        else {
            HostEntity exist = mRepository.getSync(entity.getHost());
            if (exist != null && exist.getCreated() != entity.getCreated())
                error = getApplication().getString(R.string.host_already_exist);
        }
        mErrorIp.postValue(error);
        return error == null;
    }

    private boolean checkFrequency(HostEntity entity) {
        String error = null;
        if (entity.getFrequency() < 100)
            error = getApplication().getString(R.string.frequency_must_be_more_hundred_ms);
        mErrorFrequency.postValue(error);
        return error == null;
    }

    private boolean checkTimeout(HostEntity entity) {
        String error = null;
        if (entity.getTimeout() < 1000)
            error = getApplication().getString(R.string.timeout_must_be_more_one_second);
        mErrorTimeout.postValue(error);
        return error == null;
    }
}
