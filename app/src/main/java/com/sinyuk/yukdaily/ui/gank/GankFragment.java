package com.sinyuk.yukdaily.ui.gank;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.base.LazyFragment;
import com.sinyuk.yukdaily.databinding.GankFragmentBinding;

/**
 * Created by Sinyuk on 16.10.20.
 */

public class GankFragment extends LazyFragment {
    private GankFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.gank_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    protected void fetchData() {

    }
}
