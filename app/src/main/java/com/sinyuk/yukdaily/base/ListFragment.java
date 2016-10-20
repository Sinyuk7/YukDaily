package com.sinyuk.yukdaily.base;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sinyuk.myutils.system.NetWorkUtils;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.databinding.ListFragmentBinding;

/**
 * Created by Sinyuk on 2016/10/17.
 */

public abstract class ListFragment extends BaseFragment {
    protected static final int PRELOAD_THRESHOLD = 3;

    protected ListFragmentBinding binding;
    private boolean isLoading;

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
                    isLoading = true;
                    startRefresh();
                    if (NetWorkUtils.isNetworkConnection(getContext())) {
                        refreshData();
                    } else {
                        assertError("");
                        stopRefresh();
                    }
                });
    }

    protected RecyclerView.OnScrollListener getLoadMoreListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (isLoading) {
                    return;
                }
                final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                boolean isBottom =
                        layoutManager.findLastCompletelyVisibleItemPosition() >= recyclerView.getAdapter().getItemCount() - PRELOAD_THRESHOLD;
                if (isBottom) {
                    fetchData();
                }
            }
        };
    }

    @CallSuper
    protected void assertError(String message) {
        binding.errorLayout.setErrorMessage(message);
        binding.viewAnimator.setDisplayedChildId(R.id.errorLayout);
    }

    public void onClickEmpty() {
        startRefresh();
    }

    public void onClickError() {
        startRefresh();
    }

    @CallSuper
    protected void assertEmpty(String message) {
        binding.emptyLayout.setEmptyMessage(message);
        binding.viewAnimator.setDisplayedChildId(R.id.emptyLayout);
    }

    public void startRefresh() {
        binding.viewAnimator.setDisplayedChildId(R.id.listLayout);

        if (binding.listLayout.swipeRefreshLayout != null) {
            binding.listLayout.swipeRefreshLayout.setRefreshing(true);
        }
    }

    public void stopRefresh() {
        if (binding.listLayout.swipeRefreshLayout != null) {
            isLoading = false;
            binding.listLayout.swipeRefreshLayout.postDelayed(() -> {
                if (binding.listLayout.swipeRefreshLayout != null) {
                    binding.listLayout.swipeRefreshLayout.setRefreshing(false);
                }
            }, 600);
        }
    }


    protected abstract void refreshData();

    protected abstract void fetchData();

}
