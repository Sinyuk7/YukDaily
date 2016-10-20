package com.sinyuk.yukdaily.ui.news;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.sinyuk.yukdaily.App;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.api.NewsService;
import com.sinyuk.yukdaily.base.ListFragment;
import com.sinyuk.yukdaily.databinding.NewsHeaderLayoutBinding;
import com.sinyuk.yukdaily.model.LatestNews;
import com.sinyuk.yukdaily.utils.cardviewpager.ShadowTransformer;
import com.sinyuk.yukdaily.utils.rx.SchedulerTransformer;
import com.sinyuk.yukdaily.widgets.CircleIndicator;

import javax.inject.Inject;

import rx.Observer;

/**
 * Created by Sinyuk on 2016/10/12.
 */

public class NewsFragment extends ListFragment {
    @Inject
    NewsService newsService;
    private NewsHeaderLayoutBinding headerBinding;

    @BindingAdapter({"adapter", "indicator"})
    public static void setAdapter(ViewPager vp, CardPagerAdapter adapter, CircleIndicator indicator) {
        Log.d(TAG, "setAdapter: ");
        vp.setOffscreenPageLimit(3);
        vp.setAdapter(adapter);
        indicator.setViewPager(vp);
        adapter.registerDataSetObserver(indicator.getDataSetObserver());

        final ShadowTransformer transformer = new ShadowTransformer(vp, adapter);
        vp.setPageTransformer(false, transformer);
        transformer.enableScaling(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        App.get(context).getAppComponent().inject(this);
    }


    @Override
    protected void refreshData() {
        newsService.getLatestNews()
                .compose(new SchedulerTransformer<>())
                .subscribe(new Observer<LatestNews>() {
                    @Override
                    public void onCompleted() {
                        if (binding.listLayout.recyclerView.getAdapter().getItemCount() <= 0) {
                            assertEmpty(getString(R.string.no_news));
                        } else {
                            final int count = headerBinding.viewPager.getChildCount();
                            if (count >= 3) {
                                headerBinding.viewPager.setCurrentItem(count / 2 + 1);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        assertEmpty(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(LatestNews latestNews) {
                        headerBinding.getAdapter().setData(latestNews.getTopStories());
                        ((NewsAdapter) binding.listLayout.recyclerView.getAdapter()).setData(latestNews.getStories());
                    }
                });
    }

    @Override
    protected void fetchData() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initListLayout();
        initListView();
        initListData();
    }

    private void bindHeaderView() {
        headerBinding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()), R.layout.news_header_layout, binding.listLayout.recyclerView, false);

        headerBinding.setContext(getContext());

        headerBinding.setAdapter(new CardPagerAdapter());

    }


    private void initListView() {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setAutoMeasureEnabled(true);
        binding.listLayout.recyclerView.setLayoutManager(manager);
        binding.listLayout.recyclerView.setHasFixedSize(true);
    }

    private void initListData() {
        final NewsAdapter newsAdapter = new NewsAdapter();
        newsAdapter.setHasStableIds(true);
        bindHeaderView();
        newsAdapter.addHeaderBinding(headerBinding);
        binding.listLayout.recyclerView.setAdapter(newsAdapter);
    }
}
