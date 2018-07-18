package com.xiiilab.metrix;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.xiiilab.metrix.viewmodel.ListViewModel;

public class MetricsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListViewModel listViewModel = ViewModelProviders.of(this).get(ListViewModel.class);
        listViewModel.setDetailAvailable(findViewById(R.id.detail_fragment) != null);
        setContentView(R.layout.metrics_activity);
    }
}
