package com.xiiilab.ping.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.xiiilab.ping.ping.PingRequestExecutor;
import com.xiiilab.ping.R;
import com.xiiilab.ping.repository.Repository;
import com.xiiilab.ping.viewmodel.ItemViewModel;

public class HostDetailActivity extends AppCompatActivity {

    public static final String HOST = "com.xiiilab.ping.activity.HostDetailActivity HOST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String host = getSelectedHost();
        PingRequestExecutor.getInstance().setTrackedHost(host);

        ItemViewModel viewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
        viewModel.setEntity(Repository.getInstance().getAsync(host));

        setContentView(R.layout.host_detail_activity);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PingRequestExecutor.getInstance().resetTracking();
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
