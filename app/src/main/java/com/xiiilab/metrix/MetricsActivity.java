package com.xiiilab.metrix;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.xiiilab.metrix.persistance.MetricEntity;
import com.xiiilab.metrix.viewmodel.ItemViewModel;
import com.xiiilab.metrix.viewmodel.ListViewModel;

public class MetricsActivity extends AppCompatActivity {

    private static final String SELECTED_ID = "com.xiiilab.metrix.MetricsActivity SELECTED_ID";

    private int mSelectedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListViewModel listViewModel = ViewModelProviders.of(this).get(ListViewModel.class);
        listViewModel.setRepository(Repository.getInstance());

        setContentView(R.layout.metrics_activity);

        if (findViewById(R.id.detail_fragment) == null)
            listViewModel.setDetailAvailable(false);
        else {
            listViewModel.setDetailAvailable(true);
            ItemViewModel itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
            itemViewModel.setEntity(listViewModel.getSelected());
        }

        listViewModel.getSelected().observe(this, this::setSelectedId);

        if (savedInstanceState != null)
            listViewModel.select(Repository.getInstance().get(savedInstanceState.getInt(SELECTED_ID)));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_ID, mSelectedId);
    }

    private void setSelectedId(MetricEntity entity) {
        mSelectedId = entity.getId();
    }
}
