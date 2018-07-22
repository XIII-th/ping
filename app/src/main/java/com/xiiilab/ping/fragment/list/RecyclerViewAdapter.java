package com.xiiilab.ping.fragment.list;

import android.arch.lifecycle.LifecycleOwner;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.xiiilab.ping.R;
import com.xiiilab.ping.databinding.ListItemBinding;
import com.xiiilab.ping.viewmodel.ListViewModel;

import java.util.Collections;
import java.util.List;

/**
 * Created by Sergey on 17.07.2018
 */
class RecyclerViewAdapter extends RecyclerView.Adapter<BindingViewHolder> {

    private final LifecycleOwner mLifecycleOwner;
    private final ListViewModel mListViewModel;
    private final OpenHostListener mListener;
    private List<String> mHostList;

    public RecyclerViewAdapter(Fragment fragment, ListViewModel listViewModel) {
        mLifecycleOwner = fragment;
        mListViewModel = listViewModel;
        mHostList = Collections.emptyList();
        mListViewModel.hostList().observe(mLifecycleOwner, this::refreshHostList);
        mListener = new OpenHostListener(fragment.getContext(), mListViewModel);
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
        holder.binding.setListener(mListener);
    }

    @Override
    public int getItemCount() {
        return mHostList.size();
    }

    private void refreshHostList(List<String> hostList) {
        List<String> oldList = mHostList;
        mHostList = hostList;
        // notify new hosts
        searchDiff(mHostList, oldList, this::notifyItemInserted);
        // notify about removals
        searchDiff(oldList, mHostList, this::notifyItemRemoved);
    }

    private void searchDiff(List<String> outer, List<String> inner, DiffConsumer diffConsumer) {
        outer : for (int i = 0; i < outer.size(); i++) {
            for (String newHost : inner)
                if (outer.get(i).equals(newHost))
                    continue outer;
            diffConsumer.accept(i);
        }
    }

    // TODO: 22.07.2018 replace to Consumer
    private interface DiffConsumer {
        void accept(int position);
    }
}
