package com.xiiilab.ping.fragment.edit;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.xiiilab.ping.databinding.FragmentEditBinding;
import com.xiiilab.ping.viewmodel.EditViewModel;


public class EditFragment extends Fragment {

    private EditViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity == null)
            throw new IllegalStateException("Unable to create fragment without activity");
        mViewModel = ViewModelProviders.of(activity).get(EditViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentEditBinding binding = FragmentEditBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setVm(mViewModel);
        return binding.getRoot();
    }

}
