package com.xiiilab.metrix.fragment.list;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.Intent;
import com.xiiilab.metrix.DetailActivity;
import com.xiiilab.metrix.persistance.MetricEntity;
import com.xiiilab.metrix.viewmodel.ListViewModel;

/**
 * Created by Sergey on 18.07.2018
 */
public class OpenMetricListener {

    private final Context mContext;
    private final ListViewModel mListViewModel;

    public OpenMetricListener(Context context, ListViewModel listViewModel) {
        mContext = context;
        mListViewModel = listViewModel;
    }

    public void onClick(LiveData<MetricEntity> selectedSource) {
        if (mListViewModel.isDetailAvailable())
            mListViewModel.select(selectedSource);
        else {
            Intent intent = new Intent(mContext, DetailActivity.class);
            MetricEntity entity = selectedSource.getValue();
            if (entity == null)
                throw new IllegalStateException("Selected live data source has not entity");
            intent.putExtra(DetailActivity.METRIC_KEY, entity.getId());
            mContext.startActivity(intent);
        }
    }
}
