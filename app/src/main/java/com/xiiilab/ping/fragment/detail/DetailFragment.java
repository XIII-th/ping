package com.xiiilab.ping.fragment.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.xiiilab.ping.R;
import com.xiiilab.ping.databinding.HostDetailFragmentBinding;
import com.xiiilab.ping.viewmodel.DetailViewModel;

/**
 * Created by XIII-th on 18.07.2018
 */
public class DetailFragment extends Fragment {

    private DetailViewModel mDetailViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() == null)
            throw new IllegalStateException("Unable to get activity");
        mDetailViewModel = ViewModelProviders.of(getActivity()).get(DetailViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        HostDetailFragmentBinding binding = HostDetailFragmentBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setVm(mDetailViewModel);

        // this subscription required to call chart.invalidate()
        mDetailViewModel.getLastEntry().observe(this, new ChartUpdater(binding.pingChart));

        binding.pingChart.getDescription().setText(
                getString(R.string.chart_values_count, getResources().getInteger(R.integer.chart_entry_limit)));
        return binding.getRoot();
    }
}
