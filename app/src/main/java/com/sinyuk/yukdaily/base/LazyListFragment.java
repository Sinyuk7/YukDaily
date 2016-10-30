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

/**
 * Created by Sinyuk on 16.10.30.
 */

public abstract class LazyListFragment extends BaseFragment {
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    protected int PRELOAD_THRESHOLD = 3;
    protected ListFragmentBinding binding;
    protected boolean isRefreshing = true;
    protected boolean isLoading;
    protected boolean isViewInitiated;
    protected boolean isVisibleToUser;
    protected boolean isDataInitiated;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData();
    }

    public boolean prepareFetchData() {
        return prepareFetchData(false);
    }

    public boolean prepareFetchData(boolean forceUpdate) {
        if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            refreshData();
            isDataInitiated = true;
            return true;
        }
        return false;
    }

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
    protected void onClickEmpty() {
        startRefreshing();
    }

    @CallSuper
    protected void onClickError() {
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
    protected void startRefreshing() {
        binding.viewAnimator.setDisplayedChildId(R.id.listLayout);
        if (binding.listLayout.swipeRefreshLayout != null) {
            binding.listLayout.swipeRefreshLayout.setRefreshing(true);
        }
    }

    @CallSuper
    protected void stopRefreshing() {
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
    protected void startLoading() {
        isLoading = true;
        fetchData();
    }

    @CallSuper
    protected void stopLoading() {
        isLoading = false;
    }


    protected abstract void refreshData();

    protected abstract void fetchData();


}
