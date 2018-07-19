package com.xiiilab.metrix.fragment.list;

import android.support.v7.widget.RecyclerView;
import com.xiiilab.metrix.databinding.ListItemBinding;

/**
 * Created by Sergey on 17.07.2018
 */
class BindingViewHolder extends RecyclerView.ViewHolder {

    public final ListItemBinding binding;

    public BindingViewHolder(ListItemBinding bindingView) {
        super(bindingView.getRoot());
        binding = bindingView;
    }
}
