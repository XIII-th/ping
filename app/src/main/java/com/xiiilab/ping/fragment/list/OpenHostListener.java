package com.xiiilab.ping.fragment.list;

import android.content.Context;
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

    public void onClick(String host) {
        throw new UnsupportedOperationException("Not implemented yet");
//        mListViewModel.select(mRepository.get(host));
//        if (!mListViewModel.isDetailAvailable()) {
//            Intent intent = new Intent(mContext, HostDetailActivity.class);
//            HostEntity entity = selectedSource.getValue();
//            if (entity == null)
//                throw new IllegalStateException("Selected live data source has not entity");
//            intent.putExtra(HostDetailActivity.HOST, entity.getHost());
//            mContext.startActivity(intent);
//        }
    }
}
