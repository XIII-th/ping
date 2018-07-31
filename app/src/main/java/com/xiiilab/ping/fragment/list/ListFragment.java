package com.xiiilab.ping.fragment.list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.xiiilab.ping.R;
import com.xiiilab.ping.activity.HostDetailActivity;
import com.xiiilab.ping.persistance.HostEntity;
import com.xiiilab.ping.viewmodel.ListViewModel;

/**
 * Created by XIII-th on 18.07.2018
 */
public class ListFragment extends Fragment {

    private ListViewModel mListViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() == null)
            throw new IllegalStateException("Unable to get activity");
        mListViewModel = ViewModelProviders.of(getActivity()).get(ListViewModel.class);
        mListViewModel.getSelected().observe(this, this::onHostSelected);
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

    private void onHostSelected(@Nullable HostEntity entity) {
        if (!mListViewModel.isDetailAvailable() && entity != null) {
            Context context = getContext();
            if (context == null)
                throw new IllegalStateException("Unable to start detail activity without context");
            Intent intent = new Intent(context, HostDetailActivity.class);
            intent.putExtra(HostDetailActivity.HOST, entity.getHost());
            context.startActivity(intent);
        }
    }
}
