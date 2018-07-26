package com.xiiilab.ping.activity;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.xiiilab.ping.R;
import com.xiiilab.ping.persistance.HostEntity;
import com.xiiilab.ping.ping.PingRequestExecutor;
import com.xiiilab.ping.repository.Repository;
import com.xiiilab.ping.viewmodel.DetailViewModel;
import com.xiiilab.ping.viewmodel.ItemViewModel;
import com.xiiilab.ping.viewmodel.ListViewModel;

public class HostListActivity extends AppCompatActivity {

    private static final String SELECTED_HOST = "com.xiiilab.ping.activity.HostListActivity SELECTED_HOST";

    private String mSelectedHost;

    public void onAddHostButtonPressed(View v) {
        startActivity(new Intent(this, EditActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListViewModel listViewModel = ViewModelProviders.of(this).get(ListViewModel.class);
        listViewModel.setRepository(Repository.getInstance());
        listViewModel.setItemViewModelProvider(this::getItemViewModel);

        setContentView(R.layout.host_list_activity);

        if (findViewById(R.id.detail_fragment) == null)
            listViewModel.setDetailAvailable(false);
        else {
            listViewModel.setDetailAvailable(true);
            DetailViewModel detailViewModel = getDetailViewModel();
            detailViewModel.setEntity(listViewModel.getSelected());
        }

        listViewModel.getSelected().observe(this, this::setSelectedHost);

        if (savedInstanceState != null)
            listViewModel.select(Repository.getInstance().get(savedInstanceState.getString(SELECTED_HOST)));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SELECTED_HOST, mSelectedHost);
    }

    private void setSelectedHost(HostEntity entity) {
        mSelectedHost = entity == null ? null : entity.getHost();
    }

    private ItemViewModel getItemViewModel(String key) {
        return getViewModel(key, ItemViewModel.class);
    }

    private DetailViewModel getDetailViewModel() {
        return getViewModel(null, DetailViewModel.class);
    }

    private <T extends ItemViewModel> T getViewModel(@Nullable String key, Class<T> cls) {
        ViewModelProvider provider = ViewModelProviders.of(this);
        T viewModel = key == null ?
                provider.get(cls) :
                provider.get(key, cls);
        viewModel.setRepository(Repository.getInstance());
        viewModel.setPingValueProvider(PingRequestExecutor.getInstance()::getPingValue);
        return viewModel;
    }
}
