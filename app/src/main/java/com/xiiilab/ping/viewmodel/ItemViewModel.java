package com.xiiilab.ping.viewmodel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Transformations;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import com.xiiilab.ping.activity.EditActivity;
import com.xiiilab.ping.persistance.HostEntity;
import com.xiiilab.ping.repository.Repository;

/**
 * Created by Sergey on 17.07.2018
 */
public class ItemViewModel extends AndroidViewModel {

    private final MediatorLiveData<HostEntity> mEntity;
    private LiveData<HostEntity> mActiveEntitySource;

    private final LiveData<Integer> mPingValue;

    private Repository mRepository;
    private Function<String, LiveData<Integer>> mPingValueProvider;

    public ItemViewModel(@NonNull Application application) {
        super(application);
        mEntity = new MediatorLiveData<>();
        mPingValue = Transformations.switchMap(mEntity, entity ->
                entity == null ? null : mPingValueProvider.apply(entity.getHost()));
    }

    public void setRepository(Repository repository) {
        mRepository = repository;
    }

    public void setEntity(LiveData<HostEntity> entityLiveData) {
        mEntity.removeSource(mActiveEntitySource);
        mEntity.addSource(mActiveEntitySource = entityLiveData, mEntity::setValue);
    }

    public LiveData<HostEntity> getEntity() {
        return mEntity;
    }

    public void setPingValueProvider(@NonNull Function<String, LiveData<Integer>> provider) {
        mPingValueProvider = provider;
    }

    public LiveData<Integer> getPingValue() {
        return mPingValue;
    }

    public void onEditClicked(View ignored) {
        Intent intent = new Intent(getApplication(), EditActivity.class);
        intent.putExtra(EditActivity.EDIT_ENTITY_ID, mEntity.getValue().getHost());
        getApplication().startActivity(intent);
    }

    public void onDeleteClicked(View ignored) {
        mRepository.delete(mEntity.getValue());
    }
}
