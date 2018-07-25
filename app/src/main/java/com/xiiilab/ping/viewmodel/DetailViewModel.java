package com.xiiilab.ping.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.stealthcopter.networktools.ping.PingResult;
import com.xiiilab.ping.R;
import com.xiiilab.ping.persistance.HostEntity;

import java.util.ArrayList;

/**
 * Created by Sergey on 25.07.2018
 */
public class DetailViewModel extends ItemViewModel {

    private final LiveData<Entry> mLastEntry;
    private final LineDataSet mDataSet;
    private final int mEntryLimit;

    public DetailViewModel(@NonNull Application application) {
        super(application);

        mLastEntry = Transformations.map(getPingValue(), this::addChartEntry);

        mEntryLimit = application.getResources().getInteger(R.integer.chart_entry_limit);
        mDataSet = new LineDataSet(new ArrayList<>(), application.getString(R.string.chart_legend));
        // add initial entry
        addChartEntry(null);
        LineData lineData = new LineData(mDataSet);
        mDataSet.setDrawCircleHole(false);
        lineData.setDrawValues(false);
    }

    public LiveData<Entry> getLastEntry() {
        return mLastEntry;
    }

    public LineData getChartData() {
        return new LineData(mDataSet);
    }

    private Entry addChartEntry(@Nullable PingResult pingResult) {
        HostEntity hostEntity = getEntity().getValue();
        float x, ping = pingResult == null ? 0F : pingResult.timeTaken;
        Entry lastEntry = mLastEntry.getValue();
        if (lastEntry == null)
            x = 0F;
        else {
            float pingOrTimeout = ping == 0F ? hostEntity.getTimeout() : ping;
            x = lastEntry.getX() + (hostEntity.getFrequency() + pingOrTimeout) / 1000F;
        }
        Entry entry = new Entry(x, ping);
        mDataSet.addEntry(entry);
        if (mDataSet.getEntryCount() == mEntryLimit)
            mDataSet.removeFirst();
        return entry;
    }
}
