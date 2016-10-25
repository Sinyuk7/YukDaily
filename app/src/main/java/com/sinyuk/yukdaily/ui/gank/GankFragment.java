package com.sinyuk.yukdaily.ui.gank;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sinyuk.yukdaily.App;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.base.ListFragment;
import com.sinyuk.yukdaily.data.gank.GankRepository;
import com.sinyuk.yukdaily.data.gank.GankRepositoryModule;
import com.sinyuk.yukdaily.databinding.GankFragmentBinding;
import com.sinyuk.yukdaily.entity.Gank.GankResult;

import javax.inject.Inject;

import dagger.Lazy;
import rx.Observer;

/**
 * Created by Sinyuk on 16.10.20.
 */

public class GankFragment extends ListFragment {
    @Inject
    Lazy<GankRepository> gankRepository;
    private GankFragmentBinding binding;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        App.get(context).getAppComponent().plus(new GankRepositoryModule()).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.gank_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        refreshData();
    }

    @Override
    protected void refreshData() {
        gankRepository.get().getGankAt(0, true)
                .subscribe(new Observer<GankResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: ");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(GankResult gankResult) {
                        Log.d(TAG, "onNext: GankResult " + gankResult.getAndriod().toString());
                    }
                });
    }

    @Override
    protected void fetchData() {

    }
}
