package com.xiiilab.ping.fragment.list;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.xiiilab.ping.R;
import com.xiiilab.ping.viewmodel.ListViewModel;

public class ListFragment extends Fragment {

    private ListViewModel mListViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() == null)
            throw new IllegalStateException("Unable to get activity");
        mListViewModel = ViewModelProviders.of(getActivity()).get(ListViewModel.class);
        // TODO: Use the ViewModel. Set selected item
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.host_list_fragment, container, false);
        rv.setAdapter(new RecyclerViewAdapter(this, mListViewModel));
        return rv;
    }
}
