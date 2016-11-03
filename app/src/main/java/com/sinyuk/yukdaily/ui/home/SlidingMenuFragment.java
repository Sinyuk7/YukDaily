package com.sinyuk.yukdaily.ui.home;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.sinyuk.yukdaily.App;
import com.sinyuk.yukdaily.BR;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.Sinyuk;
import com.sinyuk.yukdaily.base.BaseFragment;
import com.sinyuk.yukdaily.theme.DarkThemeComponent;
import com.sinyuk.yukdaily.theme.LightThemeComponent;

import javax.inject.Inject;

/**
 * Created by Sinyuk on 2016/10/17.
 */

public class SlidingMenuFragment extends BaseFragment {
    @Inject
    RxSharedPreferences preferences;
    private ViewDataBinding binding;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        App.get(context).getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.sliding_menu_fragment, container, false);
        binding.setVariable(BR.presenter, this);
        return binding.getRoot();
    }

    public void onClickSettings(View v) {
        if (preferences.getBoolean(Sinyuk.KEY_IS_LIGHT_THEME).isSet()) {
            if (preferences.getBoolean(Sinyuk.KEY_IS_LIGHT_THEME).get()) {
                DataBindingUtil.setDefaultComponent(new LightThemeComponent());
                preferences.getBoolean(Sinyuk.KEY_IS_LIGHT_THEME).set(true);
            }
        } else {
            DataBindingUtil.setDefaultComponent(new DarkThemeComponent());
            preferences.getBoolean(Sinyuk.KEY_IS_LIGHT_THEME).set(false);
        }

        getActivity().recreate();
    }
}
