package com.xiiilab.ping.viewmodel;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import com.xiiilab.ping.persistance.HostEntity;
import com.xiiilab.ping.repository.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * Created by XIII-th on 18.07.2018
 */
public class ListViewModel extends ViewModel {

    private final MediatorLiveData<List<String>> mHostList = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> mEmptyList = new MediatorLiveData<>();
    private final MediatorLiveData<HostEntity> mSelected = new MediatorLiveData<>();
    private final HashMap<String, ItemViewModel> mItemViewModels = new HashMap<>();
    private LiveData<HostEntity> mSelectedSource;
    private Repository mRepository;
    private Function<String, ItemViewModel> mItemViewModelProvider;
    private boolean mDetailAvailable;

    public ListViewModel() {
        mEmptyList.addSource(mHostList, list -> mEmptyList.setValue(list == null || list.isEmpty()));
    }

    public boolean isInitialised() {
        return mRepository != null && mItemViewModelProvider != null;
    }

    public void setRepository(Repository repository) {
        if (mRepository != null)
            throw new IllegalStateException("Unable to set repository twice");
        mRepository = repository;
        mHostList.addSource(mRepository.hostList(), mHostList::setValue);
    }

    public void setItemViewModelProvider(Function<String, ItemViewModel> provider) {
        mItemViewModelProvider = provider;
    }

    public void select(LiveData<HostEntity> item) {
        mSelected.removeSource(mSelectedSource);
        mSelected.addSource(mSelectedSource = item, mSelected::setValue);
    }

    public LiveData<HostEntity> getSelected() {
        return mSelected;
    }

    public ItemViewModel getItem(String host) {
        // TODO: reuse itemViewModels like view holder in recycler view
        ItemViewModel itemViewModel = mItemViewModels.get(host);
        if (itemViewModel == null) {
            if (mItemViewModelProvider == null)
                throw new IllegalStateException("Item view model provider is not specified");
            mItemViewModels.put(host, itemViewModel = mItemViewModelProvider.apply(host));
        }
        itemViewModel.setEntity(mRepository.get(host));
        return itemViewModel;
    }

    public LiveData<List<String>> hostList() {
        return mHostList;
    }

    public LiveData<Boolean> isEmpty() {
        return mEmptyList;
    }

    public boolean isDetailAvailable() {
        return mDetailAvailable;
    }

    public void setDetailAvailable(boolean detailAvailable) {
        mDetailAvailable = detailAvailable;
    }

    public void onSelectHost(ItemViewModel itemViewModel) {
        select(itemViewModel.getEntity());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mSelected.setValue(null);
        // TODO: cleanup item view models
        mRepository = null;
    }
}
