package com.sinyuk.yukdaily.ui.menu;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.base.BaseFragment;

/**
 * Created by Sinyuk on 2016/10/17.
 */

public class SlidingMenuFragment extends BaseFragment {
    private ViewDataBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.sliding_menu_fragment, container, false);
        return binding.getRoot();
    }
}
