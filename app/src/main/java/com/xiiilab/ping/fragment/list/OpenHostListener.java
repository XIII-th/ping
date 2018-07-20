package com.xiiilab.ping.fragment.list;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.Intent;
import com.xiiilab.ping.HostDetailActivity;
import com.xiiilab.ping.persistance.HostEntity;
import com.xiiilab.ping.viewmodel.ListViewModel;

/**
 * Created by Sergey on 18.07.2018
 */
public class OpenHostListener {

    private final Context mContext;
    private final ListViewModel mListViewModel;

    public OpenHostListener(Context context, ListViewModel listViewModel) {
        mContext = context;
        mListViewModel = listViewModel;
    }

    public void onClick(LiveData<HostEntity> selectedSource) {
        mListViewModel.select(selectedSource);
        if (!mListViewModel.isDetailAvailable()) {
            Intent intent = new Intent(mContext, HostDetailActivity.class);
            HostEntity entity = selectedSource.getValue();
            if (entity == null)
                throw new IllegalStateException("Selected live data source has not entity");
            intent.putExtra(HostDetailActivity.HOST, entity.getHost());
            mContext.startActivity(intent);
        }
    }
}
