package com.xiiilab.metrix;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.xiiilab.metrix.viewmodel.ItemViewModel;
import com.xiiilab.metrix.viewmodel.ListViewModel;

public class MetricsActivity extends AppCompatActivity {

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
    }
}
