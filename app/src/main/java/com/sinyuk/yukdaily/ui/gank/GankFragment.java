package com.sinyuk.yukdaily.ui.gank;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.sinyuk.yukdaily.App;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.base.ListFragment;
import com.sinyuk.yukdaily.data.gank.GankRepository;
import com.sinyuk.yukdaily.data.gank.GankRepositoryModule;
import com.sinyuk.yukdaily.entity.Gank.GankResult;
import com.sinyuk.yukdaily.utils.recyclerview.ListItemMarginDecoration;

import javax.inject.Inject;

import dagger.Lazy;
import rx.Observer;

/**
 * Created by Sinyuk on 16.10.20.
 */

public class GankFragment extends ListFragment {
    @Inject
    Lazy<GankRepository> gankRepository;
    private Observer<GankResult> refreshObserver = new Observer<GankResult>() {
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
        public void onNext(GankResult gankResult) {
            if (gankResult != null) {
                ((GankAllAdapter) binding.listLayout.recyclerView.getAdapter()).setData(gankResult);
            }
        }
    };

    private int fromToday = 1;
    private final Observer<GankResult> loadObserver = new Observer<GankResult>() {
        @Override
        public void onCompleted() {
            fromToday++;
        }

        @Override
        public void onError(Throwable e) {
            assertError(e.getLocalizedMessage());
        }

        @Override
        public void onNext(GankResult gankResult) {
            if (gankResult != null) {
                ((GankAllAdapter) binding.listLayout.recyclerView.getAdapter()).appendData(gankResult);
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
        binding.listLayout.recyclerView.setHasFixedSize(true);
        binding.listLayout.recyclerView.addItemDecoration(new ListItemMarginDecoration(1, R.dimen.content_space_8, false, getContext()));
        binding.listLayout.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (isRefreshing || isLoading) {
                    return;
                }
                final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                boolean isBottom =
                        layoutManager.findLastVisibleItemPosition() == recyclerView.getAdapter().getItemCount() - 1;
                Log.d(TAG, "onScrolled: last" + layoutManager.findLastVisibleItemPosition());
                Log.d(TAG, "onScrolled: count" + (recyclerView.getAdapter().getItemCount() - 1));
                if (isBottom) {
                    startLoading();
                }
            }
        });
    }

    private void initListData() {
        binding.listLayout.recyclerView.setAdapter(new GankAllAdapter());
    }

    @Override
    protected void refreshData() {
        addSubscription(gankRepository.get().getGankAt(0, true)
                .doOnTerminate(() -> fromToday = 1)
                .doOnTerminate(this::stopRefreshing)
                .subscribe(refreshObserver));
    }

    @Override
    protected void fetchData() {
        Log.d(TAG, "fetchData: ");
        addSubscription(gankRepository.get()
                .getGankAt(fromToday, true)
                .doOnTerminate(this::startLoading)
                .subscribe(loadObserver));
    }
}
