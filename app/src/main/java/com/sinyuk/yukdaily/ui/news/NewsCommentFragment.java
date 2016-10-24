package com.sinyuk.yukdaily.ui.news;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.sinyuk.yukdaily.App;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.base.LoadingListFragment;
import com.sinyuk.yukdaily.data.news.NewsRepository;
import com.sinyuk.yukdaily.data.news.NewsRepositoryModule;
import com.sinyuk.yukdaily.entity.news.NewsComment;
import com.sinyuk.yukdaily.utils.binding.BindingViewHolder;
import com.sinyuk.yukdaily.utils.recyclerview.InsetDividerDecoration;

import java.util.List;

import javax.inject.Inject;

import rx.Observer;

/**
 * Created by Sinyuk on 16.10.24.
 */

public class NewsCommentFragment extends LoadingListFragment {

    public static final String TAG = "NewsCommentFragment";
    private static final String KEY_ID = "ID";

    @Inject
    NewsRepository newsRepository;

    private int id;


    public static NewsCommentFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt(KEY_ID, id);
        NewsCommentFragment fragment = new NewsCommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        App.get(context).getAppComponent().plus(new NewsRepositoryModule()).inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();

        startLoading();
    }


    private void initRecyclerView() {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setAutoMeasureEnabled(true);
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.addItemDecoration(new InsetDividerDecoration(
                BindingViewHolder.class,
                getResources().getDimensionPixelSize(R.dimen.divider_height),
                getResources().getDimensionPixelSize(R.dimen.news_comment_item_keyline_1),
                ContextCompat.getColor(getContext(), R.color.divider_color)));
//        binding.recyclerView.addOnScrollListener(getLoadMoreListener());
    }


    @Override
    protected void refreshData() {
        id = getArguments().getInt(KEY_ID);

        addSubscription(newsRepository.getNewsAllComments(id).subscribe(new Observer<List<NewsComment>>() {
            @Override
            public void onCompleted() {
                if (binding.recyclerView.getAdapter().getItemCount() == 0) {
                    assertEmpty(getString(R.string.no_comment));
                } else {
                    showList();
                }
            }

            @Override
            public void onError(Throwable e) {
                assertError(e.getLocalizedMessage());
            }

            @Override
            public void onNext(List<NewsComment> newsComments) {
                Log.d(TAG, "onNext: " + newsComments.toString());
                binding.recyclerView.setAdapter(new NewsCommentAdapter(getContext(), newsComments));
            }
        }));

    }

    @Override
    protected void fetchData() {
        // no-op
    }
}
