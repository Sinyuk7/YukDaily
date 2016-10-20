package com.sinyuk.yukdaily.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Sinyuk on 16/9/11.
 * 实现懒加载的fragment 只只用于view pager当中
 * 因为setUserVisibleHint()方法 在fragment生命周期中不调用 需要手动调用
 */
public abstract class LazyFragment extends Fragment {
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    protected boolean isViewInitiated;
    protected boolean isVisibleToUser;
    protected boolean isDataInitiated;
    private CompositeSubscription mCompositeSubscription;

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
            fetchData();
            isDataInitiated = true;
            return true;
        }
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }
        mCompositeSubscription = new CompositeSubscription();
    }

    /**
     * 保存Fragment的Hidden状态
     * 避免内存重启导致的Fragment重叠
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    protected abstract void fetchData();

    protected void addSubscription(Subscription s) {
        mCompositeSubscription.add(s);
    }

    protected void removeSubscription(Subscription s) {
        mCompositeSubscription.remove(s);
    }

    protected void clearSubscription() {
        mCompositeSubscription.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.unsubscribe();
        }

    }

}
