package com.sinyuk.yukdaily.utils.rx;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class SchedulerTransformer<T> implements Observable.Transformer<T, T> {
    public static <T> SchedulerTransformer<T> create() {
        return new SchedulerTransformer<>();
    }

    @Override
    public Observable<T> call(Observable<T> observable) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}