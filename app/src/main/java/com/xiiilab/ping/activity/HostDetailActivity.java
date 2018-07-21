package com.xiiilab.ping.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.xiiilab.ping.DummyHostProvider;
import com.xiiilab.ping.R;
import com.xiiilab.ping.Repository;
import com.xiiilab.ping.viewmodel.ItemViewModel;

public class HostDetailActivity extends AppCompatActivity {

    public static final String HOST = "com.xiiilab.ping.activity.HostDetailActivity HOST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String host = getSelectedHost();
        DummyHostProvider.getInstance().setTrackedHost(host);

        ItemViewModel viewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
        viewModel.setEntity(Repository.getInstance().get(host));

        setContentView(R.layout.host_detail_activity);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DummyHostProvider.getInstance().resetTracking();
    }

    private String getSelectedHost() {
        String host = null;
        if (getIntent() != null)
            host = getIntent().getStringExtra(HOST);

        if (host == null)
            throw new IllegalStateException("Unable to obtain host entity from intent");

        return host;
    }
}
