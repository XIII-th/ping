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
import com.stealthcopter.networktools.ping.PingResult;
import com.xiiilab.ping.BindingConversions;
import com.xiiilab.ping.R;
import com.xiiilab.ping.activity.EditActivity;
import com.xiiilab.ping.persistance.HostEntity;
import com.xiiilab.ping.repository.Repository;

/**
 * Created by Sergey on 17.07.2018
 */
public class ItemViewModel extends AndroidViewModel {

    private final MediatorLiveData<HostEntity> mEntity;
    private LiveData<HostEntity> mActiveEntitySource;

    private final LiveData<PingResult> mPingValue;

    private Repository mRepository;
    private Function<String, LiveData<PingResult>> mPingValueProvider;

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

    public void setPingValueProvider(@NonNull Function<String, LiveData<PingResult>> provider) {
        mPingValueProvider = provider;
    }

    public LiveData<String> getTitle() {
        return Transformations.map(getEntity(), entity ->
                entity.getTitle() == null ? entity.getHost() : entity.getTitle());
    }

    public LiveData<Boolean> isTitlePresent() {
        return Transformations.map(getEntity(), entity ->
                entity.getTitle() != null);
    }

    public LiveData<String> getHost() {
        return Transformations.map(getEntity(), HostEntity::getHost);
    }

    public LiveData<String> getCurrentPing() {
        return Transformations.map(mPingValue, ping ->
                getApplication().getString(R.string.current_ping_format,
                    BindingConversions.DECIMAL_FORMAT.format(ping.timeTaken)));
    }

    public LiveData<String> getErrorString() {
        return Transformations.map(mPingValue, this::getErrorString);
    }

    public void onEditClicked(View ignored) {
        Intent intent = new Intent(getApplication(), EditActivity.class);
        intent.putExtra(EditActivity.EDIT_ENTITY_ID, mEntity.getValue().getHost());
        getApplication().startActivity(intent);
    }

    public void onDeleteClicked(View ignored) {
        mRepository.delete(mEntity.getValue());
    }

    protected LiveData<HostEntity> getEntity() {
        return mEntity;
    }

    protected LiveData<PingResult> getPingValue() {
        return mPingValue;
    }

    private String getErrorString(PingResult pingResult) {
        if (pingResult.error == null)
            return null;
        switch (pingResult.error) {
            case "failed, exit = 1":
                return getApplication().getString(R.string.ping_command_failed);
            case "error, exit = 2":
                return getApplication().getString(R.string.unable_to_execute_ping_command);
            default:
                return getApplication().getString(R.string.unexpected_ping_error);
        }
    }
}
