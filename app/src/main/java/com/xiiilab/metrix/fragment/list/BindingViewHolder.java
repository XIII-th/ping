package com.xiiilab.metrix.fragment.list;

import android.support.v7.widget.RecyclerView;
import com.xiiilab.metrix.BR;
import com.xiiilab.metrix.viewmodel.ItemViewModel;
import com.xiiilab.metrix.viewmodel.ListViewModel;
import com.xiiilab.metrix.databinding.ListItemBinding;

/**
 * Created by Sergey on 17.07.2018
 */
class BindingViewHolder extends RecyclerView.ViewHolder {

    private final ListItemBinding mBinding;

    BindingViewHolder(ListItemBinding bindingView) {
        super(bindingView.getRoot());
        mBinding = bindingView;
    }

    void bind(ListViewModel listViewModel, ItemViewModel itemViewModel) {
        mBinding.setVariable(BR.listVm, listViewModel);
        mBinding.setVariable(BR.itemVm, itemViewModel);
    }
}
