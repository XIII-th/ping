package com.xiiilab.metrix;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.xiiilab.metrix.viewmodel.ListViewModel;

public class MetricsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Repository repository = new Repository(this);
        getLifecycle().addObserver(repository);

        ListViewModel listViewModel = ViewModelProviders.of(this).get(ListViewModel.class);
        listViewModel.setRepository(repository);
        listViewModel.setDetailAvailable(findViewById(R.id.detail_fragment) != null);

        setContentView(R.layout.metrics_activity);

        DummyMetricsProvider provider = new DummyMetricsProvider(repository);
        provider.enable();
        getLifecycle().addObserver(provider);
    }
}
