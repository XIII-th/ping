package com.xiiilab.ping.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import com.xiiilab.ping.Repository;
import com.xiiilab.ping.persistance.HostEntity;

import java.util.HashMap;
import java.util.List;

public class ListViewModel extends ViewModel {

    private final MediatorLiveData<HostEntity> mSelected = new MediatorLiveData<>();
    private final HashMap<String, ItemViewModel> mItemViewModels = new HashMap<>();
    private LiveData<HostEntity> mSelectedSource;
    private Repository mRepository;
    private boolean mDetailAvailable;

    public void setRepository(Repository repository) {
        mRepository = repository;
    }

    public void select(LiveData<HostEntity> item) {
        mSelected.removeSource(mSelectedSource);
        mSelectedSource = item;
        mSelected.addSource(mSelectedSource, mSelected::setValue);
    }

    public LiveData<HostEntity> getSelected() {
        return mSelected;
    }

    public ItemViewModel getItem(String host) {
        // TODO: reuse itemViewModels like view holder in recycler view
        ItemViewModel itemViewModel = mItemViewModels.get(host);
        if (itemViewModel == null)
            mItemViewModels.put(host, itemViewModel = new ItemViewModel());
        itemViewModel.setEntity(mRepository.get(host));
        return itemViewModel;
    }

    public LiveData<List<String>> hostList() {
        return mRepository.hostList();
    }

    public boolean isDetailAvailable() {
        return mDetailAvailable;
    }

    public void setDetailAvailable(boolean mDetailAvailable) {
        this.mDetailAvailable = mDetailAvailable;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mSelected.setValue(null);
        // TODO: cleanup item view models
        mRepository = null;
    }
}
