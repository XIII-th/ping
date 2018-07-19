package com.xiiilab.metrix;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.xiiilab.metrix.viewmodel.ItemViewModel;

public class DetailActivity extends AppCompatActivity {

    public static final String METRIC_ID = "com.xiiilab.metrix.DetailActivity METRIC_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int id = getSelectedMetricId();
        DummyMetricsProvider.getInstance().setTrackedMetric(id);

        ItemViewModel viewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
        viewModel.setEntity(Repository.getInstance().get(id));

        setContentView(R.layout.activity_detail);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DummyMetricsProvider.getInstance().resetTracking();
    }

    private int getSelectedMetricId() {
        int id = -1;
        if (getIntent() != null)
            id = getIntent().getIntExtra(METRIC_ID, -1);

        if (id == -1)
            throw new IllegalStateException("Unable to obtain metric entity id from intent");

        return id;
    }
}
