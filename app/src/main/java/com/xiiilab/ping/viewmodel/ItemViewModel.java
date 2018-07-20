package com.xiiilab.ping.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.xiiilab.ping.persistance.HostEntity;

/**
 * Created by Sergey on 17.07.2018
 */
public class ItemViewModel extends ViewModel {

    private LiveData<HostEntity> mEntity;

    public void setEntity(LiveData<HostEntity> entityLiveData) {
        // TODO: use mediator instead of direct assign
        mEntity = entityLiveData;
    }

    public LiveData<HostEntity> getEntity() {
        return mEntity;
    }
}
