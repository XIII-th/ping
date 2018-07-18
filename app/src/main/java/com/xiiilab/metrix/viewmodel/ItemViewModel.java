package com.xiiilab.metrix.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.xiiilab.metrix.persistance.MetricEntity;

/**
 * Created by Sergey on 17.07.2018
 */
public class ItemViewModel extends ViewModel {

    private LiveData<MetricEntity> mEntity;

    public void setEntity(LiveData<MetricEntity> entityLiveData) {
        // TODO: use mediator instead of direct assign
        mEntity = entityLiveData;
    }

    public LiveData<MetricEntity> getEntity() {
        return mEntity;
    }
}
