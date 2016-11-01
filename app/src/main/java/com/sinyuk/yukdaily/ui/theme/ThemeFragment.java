package com.sinyuk.yukdaily.ui.theme;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.sinyuk.myutils.system.ToastUtils;
import com.sinyuk.yukdaily.App;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.base.ListFragment;
import com.sinyuk.yukdaily.data.news.NewsRepository;
import com.sinyuk.yukdaily.data.news.NewsRepositoryModule;
import com.sinyuk.yukdaily.databinding.ThemeHeaderLayoutBinding;
import com.sinyuk.yukdaily.entity.news.ThemeData;
import com.sinyuk.yukdaily.events.NewsSwitchEvent;
import com.sinyuk.yukdaily.ui.news.NewsAdapter;
import com.sinyuk.yukdaily.utils.recyclerview.ListItemMarginDecoration;
import com.sinyuk.yukdaily.utils.recyclerview.SlideInUpAnimator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import dagger.Lazy;
import rx.Observer;

/**
 * Created by Sinyuk on 16.10.31.
 */

public class ThemeFragment extends ListFragment {
    public static final String TAG = "ThemeFragment";
    @Inject
    Lazy<ToastUtils> toastUtilsLazy;
    @Inject
    NewsRepository newsRepository;
    private ThemeHeaderLayoutBinding headerBinding;
    private int index;
    private Observer<ThemeData> refreshObserver = new Observer<ThemeData>() {
        @Override
        public void onCompleted() {
            if (binding.listLayout.recyclerView.getAdapter().getItemCount() <= 0) {
                assertEmpty(getString(R.string.no_news));
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            assertEmpty(getString(R.string.network_error));
        }

        @Override
        public void onNext(ThemeData themeData) {
            Log.d(TAG, "onNext: ");
            if (headerBinding.recyclerView.getAdapter() == null) {
                Log.d(TAG, "onNext: null");
            }
            if (headerBinding != null && headerBinding.recyclerView.getAdapter() != null) {
                ((EditorAdapter) headerBinding.recyclerView.getAdapter()).setData(themeData.getEditors());
            }

            ((NewsAdapter) binding.listLayout.recyclerView.getAdapter()).setData(themeData.getStories());
        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        App.get(context).getAppComponent().plus(new NewsRepositoryModule()).inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        final NewsAdapter newsAdapter = new NewsAdapter();
        newsAdapter.setHasStableIds(true);
        bindHeaderView();
        newsAdapter.addHeaderBinding(headerBinding);
        binding.listLayout.recyclerView.setAdapter(newsAdapter);
    }

    private void bindHeaderView() {
        headerBinding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()), R.layout.theme_header_layout, binding.listLayout.recyclerView, false);

        final LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        headerBinding.recyclerView.setLayoutManager(manager);
        headerBinding.recyclerView.setHasFixedSize(true);
        headerBinding.recyclerView.addItemDecoration(new AvatarListDecoration(R.dimen.content_space_8, false, getContext()));
        headerBinding.recyclerView.setAdapter(new EditorAdapter());
    }

    @Override
    protected void refreshData() {
        addSubscription(newsRepository.getThemeData(index)
                .doOnTerminate(this::stopRefreshing)
                .subscribe(refreshObserver));
    }

    @Override
    protected void fetchData() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewsSwitch(NewsSwitchEvent event) {
        if (Integer.MIN_VALUE == (event.getIndex())) {
            // no-op
        } else if (index != event.getIndex()) {
            index = event.getIndex();
            refreshData();
        }
    }

}
