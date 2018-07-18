package com.xiiilab.metrix.fragment.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.xiiilab.metrix.BR;
import com.xiiilab.metrix.databinding.MetricDetailFragmentBinding;
import com.xiiilab.metrix.viewmodel.ItemViewModel;
import com.xiiilab.metrix.viewmodel.ListViewModel;

public class DetailFragment extends Fragment {

    private ItemViewModel mItemViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() == null)
            throw new IllegalStateException("Unable to get activity");
        ListViewModel listViewModel = ViewModelProviders.of(getActivity()).get(ListViewModel.class);
        mItemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
        mItemViewModel.setEntity(listViewModel.getSelected());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        MetricDetailFragmentBinding binding = MetricDetailFragmentBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setVariable(BR.itemVm, mItemViewModel);
        return binding.getRoot();
    }
}
