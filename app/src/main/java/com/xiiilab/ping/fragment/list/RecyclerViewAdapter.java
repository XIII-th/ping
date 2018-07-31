package com.xiiilab.ping.fragment.list;

import android.arch.lifecycle.LifecycleOwner;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.xiiilab.ping.DiffList;
import com.xiiilab.ping.R;
import com.xiiilab.ping.databinding.ListItemBinding;
import com.xiiilab.ping.viewmodel.ListViewModel;

import java.util.List;

/**
 * Created by XIII-th on 17.07.2018
 */
class RecyclerViewAdapter extends RecyclerView.Adapter<BindingViewHolder> {

    private final LifecycleOwner mLifecycleOwner;
    private final ListViewModel mListViewModel;
    private final DiffList<String> mHostList;

    public RecyclerViewAdapter(Fragment fragment, ListViewModel listViewModel) {
        mLifecycleOwner = fragment;
        mListViewModel = listViewModel;
        mHostList = new DiffList<>();
        mListViewModel.hostList().observe(mLifecycleOwner, this::refreshHostList);
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
        holder.binding.setListVm(mListViewModel);
        holder.binding.setItemVm(mListViewModel.getItem(mHostList.get(position)));
    }

    @Override
    public int getItemCount() {
        return mHostList.size();
    }

    private void refreshHostList(List<String> hostList) {
        mHostList.
                beginMigration(hostList).
                onInsert((position, data) -> notifyItemInserted(position)).
                onRemove((position, data) -> notifyItemRemoved(position)).
                completeMigration();
    }
}
