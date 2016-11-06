package com.sinyuk.yukdaily.base;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sinyuk.myutils.system.NetWorkUtils;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.databinding.ListFragmentBinding;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Sinyuk on 2016/10/17.
 */

public abstract class ListFragment extends BaseFragment {
    protected int PRELOAD_THRESHOLD = 3;

    protected ListFragmentBinding binding;
    protected boolean isRefreshing = true;
    protected boolean isLoading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.list_fragment, container, false);
        return binding.getRoot();
    }

    protected void initListLayout() {
        binding.listLayout.swipeRefreshLayout.setColorSchemeResources(android.R.color.black);
        // do not use lambda!!
        binding.listLayout.swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    isRefreshing = true;
                    startRefreshing();
                    if (NetWorkUtils.isNetworkConnection(getContext())) {
                        refreshData();
                    } else {
                        assertError("");
                        stopRefreshing();
                    }
                });
    }

    protected RecyclerView.OnScrollListener getLoadMoreListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (isRefreshing || isLoading) {
                    return;
                }
                final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                boolean isBottom =
                        layoutManager.findLastCompletelyVisibleItemPosition() >= recyclerView.getAdapter().getItemCount() - PRELOAD_THRESHOLD;
                if (isBottom) {
                    startLoading();
                }
            }
        };
    }

    @CallSuper
    protected void assertError(String message) {
        binding.errorLayout.setErrorMessage(message);
        binding.viewAnimator.setDisplayedChildId(R.id.errorLayout);
    }

    @CallSuper
    protected void onClickEmpty(View view) {
        startRefreshing();
    }

    @CallSuper
    protected void onClickError(View view) {
        startRefreshing();
    }

    @CallSuper
    protected void assertEmpty(String message) {
        binding.emptyLayout.setEmptyMessage(message);
        binding.viewAnimator.setDisplayedChildId(R.id.emptyLayout);
    }

    @CallSuper
    protected void assertNoMore(String message) {
        binding.emptyLayout.setEmptyMessage(message);
    }

    @CallSuper
    public void startRefreshing() {
        binding.viewAnimator.setDisplayedChildId(R.id.listLayout);
        if (binding.listLayout.swipeRefreshLayout != null) {
            binding.listLayout.swipeRefreshLayout.setRefreshing(true);
        }
    }

    @CallSuper
    public void stopRefreshing() {
        if (binding.listLayout.swipeRefreshLayout != null) {
            isRefreshing = false;
            binding.listLayout.swipeRefreshLayout.postDelayed(() -> {
                if (binding.listLayout.swipeRefreshLayout != null) {
                    binding.listLayout.swipeRefreshLayout.setRefreshing(false);
                }
            }, 600);
        }
    }

    @CallSuper
    public void startLoading() {
        isLoading = true;
        fetchData();
    }

    @CallSuper
    public void stopLoading() {
        isLoading = false;
    }


    protected abstract void refreshData();

    protected abstract void fetchData();

    @Subscribe()
    public void onXXXEvent() {}
}
