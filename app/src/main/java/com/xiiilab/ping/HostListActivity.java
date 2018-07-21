package com.xiiilab.ping;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.xiiilab.ping.persistance.HostEntity;
import com.xiiilab.ping.viewmodel.ItemViewModel;
import com.xiiilab.ping.viewmodel.ListViewModel;

public class HostListActivity extends AppCompatActivity {

    private static final String SELECTED_HOST = "com.xiiilab.ping.HostListActivity SELECTED_HOST";

    private String mSelectedHost;

    public void onAddHostButtonPressed(View v) {
        startActivity(new Intent(this, EditActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListViewModel listViewModel = ViewModelProviders.of(this).get(ListViewModel.class);
        listViewModel.setRepository(Repository.getInstance());

        setContentView(R.layout.host_list_activity);

        if (findViewById(R.id.detail_fragment) == null)
            listViewModel.setDetailAvailable(false);
        else {
            listViewModel.setDetailAvailable(true);
            ItemViewModel itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
            itemViewModel.setEntity(listViewModel.getSelected());
        }

        listViewModel.getSelected().observe(this, this::setSelectedHost);

        if (savedInstanceState != null)
            listViewModel.select(Repository.getInstance().get(savedInstanceState.getString(SELECTED_HOST)));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SELECTED_HOST, mSelectedHost);
    }

    private void setSelectedHost(HostEntity entity) {
        mSelectedHost = entity == null ? null : entity.getHost();
    }
}
