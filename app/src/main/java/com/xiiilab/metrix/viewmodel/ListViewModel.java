package com.xiiilab.metrix.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.SparseArray;
import com.xiiilab.metrix.persistance.DummyDataBase;
import com.xiiilab.metrix.persistance.MetricEntity;

public class ListViewModel extends ViewModel {

    private final MutableLiveData<MetricEntity> mSelected = new MutableLiveData<>();
    private final SparseArray<ItemViewModel> mItemViewModels = new SparseArray<>();
    private boolean mDetailAvailable;

    public void select(MetricEntity item) {
        mSelected.setValue(item);
    }

    public LiveData<MetricEntity> getSelected() {
        return mSelected;
    }

    public ItemViewModel getItem(int id) {
        // TODO: reuse itemViewModels like view holder in recycler view
        ItemViewModel itemViewModel = mItemViewModels.get(id, new ItemViewModel());
        itemViewModel.setEntity(DummyDataBase.instance().get(id));
        return itemViewModel;
    }

    public LiveData<Integer> count() {
        return DummyDataBase.instance().getCount();
    }

    public boolean isDetailAvailable() {
        return mDetailAvailable;
    }

    public void setDetailAvailable(boolean mDetailAvailable) {
        this.mDetailAvailable = mDetailAvailable;
    }
}
