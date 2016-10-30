package com.sinyuk.yukdaily.ui.gank;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.sinyuk.yukdaily.App;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.base.ListFragment;
import com.sinyuk.yukdaily.data.gank.GankRepository;
import com.sinyuk.yukdaily.data.gank.GankRepositoryModule;
import com.sinyuk.yukdaily.entity.Gank.GankData;
import com.sinyuk.yukdaily.utils.recyclerview.ListItemMarginDecoration;
import com.sinyuk.yukdaily.utils.recyclerview.SlideInUpAnimator;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;
import rx.Observer;

/**
 * Created by Sinyuk on 16.10.20.
 */

public class GankFragment extends ListFragment {
    private static final int PAGE_SIZE = 8;
    @Inject
    Lazy<GankRepository> gankRepository;
    private Observer<List<GankData>> refreshObserver = new Observer<List<GankData>>() {
        @Override
        public void onCompleted() {
            if (binding.listLayout.recyclerView.getAdapter().getItemCount() <= 0) {
                assertEmpty(getString(R.string.no_ganks));
            }
        }

        @Override
        public void onError(Throwable e) {
            assertError(e.getLocalizedMessage());
            e.printStackTrace();
        }

        @Override
        public void onNext(List<GankData> gankResult) {
            if (gankResult != null) {
                ((GankAdapter) binding.listLayout.recyclerView.getAdapter()).setData(gankResult);
            }
        }
    };

    private int pageIndex = 1;

    private final Observer<List<GankData>> loadObserver = new Observer<List<GankData>>() {
        @Override
        public void onCompleted() {
            pageIndex++;
        }

        @Override
        public void onError(Throwable e) {
            assertError(e.getLocalizedMessage());
        }

        @Override
        public void onNext(List<GankData> gankResult) {
            if (gankResult != null) {
                ((GankAdapter) binding.listLayout.recyclerView.getAdapter()).appendData(gankResult);
            }
        }
    };


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
        binding.listLayout.recyclerView.setItemAnimator(new SlideInUpAnimator(new FastOutSlowInInterpolator()));
        binding.listLayout.recyclerView.setHasFixedSize(true);
        binding.listLayout.recyclerView.addItemDecoration(new ListItemMarginDecoration(R.dimen.content_space_8, false, getContext()));
        binding.listLayout.recyclerView.addOnScrollListener(getLoadMoreListener());
    }


    private void initListData() {
        binding.listLayout.recyclerView.setAdapter(new GankAdapter(getContext()));
    }

    @Override
    protected void refreshData() {
        addSubscription(gankRepository.get()
                .getWhat("Android", PAGE_SIZE, 0)
                .doOnTerminate(() -> pageIndex = 1)
                .doOnTerminate(this::stopRefreshing)
                .subscribe(refreshObserver));
    }

    @Override
    protected void fetchData() {
        Log.d(TAG, "load more ganks: ");
        Log.d(TAG, "pageIndex: " + pageIndex);
        addSubscription(gankRepository.get()
                .getWhat("Android", PAGE_SIZE, pageIndex)
                .doOnTerminate(this::stopLoading)
                .subscribe(loadObserver));
    }
}
