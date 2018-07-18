package com.xiiilab.metrix.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.SparseArray;
import com.xiiilab.metrix.Repository;
import com.xiiilab.metrix.persistance.MetricEntity;

public class ListViewModel extends ViewModel {

    private final MediatorLiveData<MetricEntity> mSelected = new MediatorLiveData<>();
    private final SparseArray<ItemViewModel> mItemViewModels = new SparseArray<>();
    private LiveData<MetricEntity> mSelectedSource;
    private Repository mRepository;
    private boolean mDetailAvailable;

    public void setRepository(Repository repository) {
        mRepository = repository;
    }

    public void select(LiveData<MetricEntity> item) {
        mSelected.removeSource(mSelectedSource);
        mSelectedSource = item;
        mSelected.addSource(mSelectedSource, mSelected::setValue);
    }

    public LiveData<MetricEntity> getSelected() {
        return mSelected;
    }

    public ItemViewModel getItem(int id) {
        // TODO: reuse itemViewModels like view holder in recycler view
        ItemViewModel itemViewModel = mItemViewModels.get(id, new ItemViewModel());
        itemViewModel.setEntity(mRepository.get(id));
        return itemViewModel;
    }

    public LiveData<Integer> count() {
        return mRepository.getCount();
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
