package com.sinyuk.yukdaily.ui.gank;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.sinyuk.yukdaily.App;
import com.sinyuk.yukdaily.base.ListFragment;
import com.sinyuk.yukdaily.data.gank.GankRepository;
import com.sinyuk.yukdaily.data.gank.GankRepositoryModule;
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        App.get(context).getAppComponent().plus(new GankRepositoryModule()).inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initListLayout();
        initListView();
        initListData();

    }

    private void initListView() {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setAutoMeasureEnabled(true);
        binding.listLayout.recyclerView.setLayoutManager(manager);
        binding.listLayout.recyclerView.setHasFixedSize(true);
//        binding.listLayout.recyclerView.addOnScrollListener(getLoadMoreListener());
    }

    private void initListData() {
        binding.listLayout.recyclerView.setAdapter(new GankAllAdapter());

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
                        if (gankResult != null) {
                            ((GankAllAdapter) binding.listLayout.recyclerView.getAdapter()).setData(gankResult);                        }
                    }
                });
    }

    @Override
    protected void fetchData() {

    }
}
