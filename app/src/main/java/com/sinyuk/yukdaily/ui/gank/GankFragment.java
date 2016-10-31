package com.sinyuk.yukdaily.ui.gank;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.sinyuk.yukdaily.App;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.base.LazyListFragment;
import com.sinyuk.yukdaily.data.gank.GankRepository;
import com.sinyuk.yukdaily.data.gank.GankRepositoryModule;
import com.sinyuk.yukdaily.entity.Gank.GankData;
import com.sinyuk.yukdaily.events.GankSwitchEvent;
import com.sinyuk.yukdaily.events.ToolbarTitleChangeEvent;
import com.sinyuk.yukdaily.utils.recyclerview.ListItemMarginDecoration;
import com.sinyuk.yukdaily.utils.recyclerview.RecyclerViewPreloader;
import com.sinyuk.yukdaily.utils.recyclerview.SlideInUpAnimator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;
import rx.Observer;

/**
 * Created by Sinyuk on 16.10.20.
 */

public class GankFragment extends LazyListFragment {

    private static final int PAGE_SIZE = 8;
    @Inject
    Lazy<GankRepository> gankRepository;
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
    private Observer<List<GankData>> refreshObserver = new Observer<List<GankData>>() {
        @Override
        public void onCompleted() {
            if (binding.listLayout.recyclerView.getAdapter().getItemCount() <= 0) {
                assertEmpty(getString(R.string.no_ganks));
            }

            pageIndex++;
        }

        @Override
        public void onError(Throwable e) {
            assertError(e.getLocalizedMessage());
            e.printStackTrace();
            pageIndex = 1;
        }

        @Override
        public void onNext(List<GankData> gankResult) {
            if (gankResult != null) {
                ((GankAdapter) binding.listLayout.recyclerView.getAdapter()).setData(gankResult);
            }
        }
    };
    private String mType;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        App.get(context).getAppComponent().plus(new GankRepositoryModule()).inject(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListLayout();
        binding.listLayout.swipeRefreshLayout.setEnabled(false);
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
        GankAdapter adapter = new GankAdapter(getContext(), Glide.with(this));
        binding.listLayout.recyclerView.setAdapter(adapter);
        binding.listLayout.recyclerView.addOnScrollListener(new RecyclerViewPreloader<>(adapter, adapter, 3));
    }

    @Override
    protected void lazyDo() {
        EventBus.getDefault().post(new GankSwitchEvent("Android"));
    }

    @Override
    protected void refreshData() {
        addSubscription(gankRepository.get()
                .getWhat(mType, PAGE_SIZE, 1)
                .doOnTerminate(this::stopRefreshing)
                .subscribe(refreshObserver));
    }

    @Override
    protected void fetchData() {
        addSubscription(gankRepository.get()
                .getWhat(mType, PAGE_SIZE, pageIndex)
                .doOnTerminate(this::stopLoading)
                .subscribe(loadObserver));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGankSwitch(GankSwitchEvent event) {
        if (!TextUtils.isEmpty(event.getType()) && !event.getType().equals(mType)) {
            mType = event.getType();
            refreshData();
            EventBus.getDefault().post(new ToolbarTitleChangeEvent(event.getType()));
        }
    }


}
