package com.xiiilab.metrix.fragment.list;

import android.arch.lifecycle.LifecycleOwner;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.xiiilab.metrix.viewmodel.ListViewModel;
import com.xiiilab.metrix.R;
import com.xiiilab.metrix.databinding.ListItemBinding;

/**
 * Created by Sergey on 17.07.2018
 */
class RecyclerViewAdapter extends RecyclerView.Adapter<BindingViewHolder> {

    private final LifecycleOwner mLifecycleOwner;
    private final ListViewModel mListViewModel;
    private int mCount;

    public RecyclerViewAdapter(LifecycleOwner liveCycleOwner, ListViewModel listViewModel) {
        mLifecycleOwner = liveCycleOwner;
        mListViewModel = listViewModel;
        mListViewModel.count().observe(liveCycleOwner, this::refreshCount);
    }

    @NonNull
    @Override
    public BindingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_item, parent, false);
        binding.setLifecycleOwner(mLifecycleOwner);
        return new BindingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder holder, int position) {
        holder.bind(mListViewModel, mListViewModel.getItem(position));
    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    private void refreshCount(Integer count) {
        if (count == null)
            throw new IllegalArgumentException("Unexpected null count");
        if (mCount != count) {
            mCount = count;
            // TODO: notify certain item
            notifyDataSetChanged();
        }
    }
}
