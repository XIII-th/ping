package com.xiiilab.ping.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import com.xiiilab.ping.activity.EditActivity;
import com.xiiilab.ping.persistance.HostEntity;

/**
 * Created by Sergey on 17.07.2018
 */
public class ItemViewModel extends AndroidViewModel {

    private LiveData<HostEntity> mEntity;

    public ItemViewModel(@NonNull Application application) {
        super(application);
    }

    public void setEntity(LiveData<HostEntity> entityLiveData) {
        // TODO: use mediator instead of direct assign
        mEntity = entityLiveData;
    }

    public LiveData<HostEntity> getEntity() {
        return mEntity;
    }

    public void onEditClicked(View ignored) {
        Intent intent = new Intent(getApplication(), EditActivity.class);
        intent.putExtra(EditActivity.EDIT_ENTITY_ID, mEntity.getValue().getHost());
        getApplication().startActivity(intent);
    }
}
