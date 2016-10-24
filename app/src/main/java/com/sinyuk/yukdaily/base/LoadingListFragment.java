package com.sinyuk.yukdaily.base;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.databinding.LoadingListFragmentBinding;

/**
 * Created by Sinyuk on 16.10.24.
 */

public abstract class LoadingListFragment extends BaseSlideFragment {
    protected static final int PRELOAD_THRESHOLD = 3;

    protected LoadingListFragmentBinding binding;
    protected boolean isRefreshing = true;
    protected boolean isLoading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.loading_list_fragment, container, false);
        return binding.getRoot();
    }

    @CallSuper
    protected void starRefreshing(String message) {
        binding.viewAnimator.setDisplayedChildId(R.id.loadingLayout);
        binding.loadingLayout.loadingIndicatorView.smoothToShow();
        binding.loadingLayout.setLoadingMessage(message);
        refreshData();
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
        ActivityCompat.finishAfterTransition(getActivity());
    }

    @CallSuper
    protected void onClickError() {
        ActivityCompat.finishAfterTransition(getActivity());
    }


    @CallSuper
    protected void assertEmpty(String message) {
        binding.emptyLayout.setEmptyMessage(message);
        binding.viewAnimator.setDisplayedChildId(R.id.emptyLayout);
    }

    @CallSuper
    protected void assertNoMore(String message) {
        Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


    @CallSuper
    protected void showList() {
        binding.viewAnimator.setDisplayedChildId(R.id.listLayout);
    }


    @CallSuper
    protected void startLoading() {
        isLoading = true;
        refreshData();
    }

    @CallSuper
    protected void stopLoading() {
        isLoading = false;
    }


    protected abstract void refreshData();

    protected abstract void fetchData();
}
