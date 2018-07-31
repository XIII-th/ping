package com.xiiilab.ping.fragment.detail;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

/**
 * Created by XIII-th on 26.07.2018
 */
public class ChartUpdater implements Observer<Entry> {

    private final LineChart mChart;

    public ChartUpdater(LineChart chart) {
        mChart = chart;
    }

    @Override
    public void onChanged(@Nullable Entry entry) {
        mChart.getLineData().notifyDataChanged();
        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }
}
