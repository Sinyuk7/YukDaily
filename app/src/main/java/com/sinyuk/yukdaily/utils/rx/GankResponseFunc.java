package com.sinyuk.yukdaily.utils.rx;

import com.sinyuk.yukdaily.entity.Gank.GankResponse;

import rx.functions.Func1;

/**
 * 用来统一处理gank回调中的error
 */
public final class GankResponseFunc<T> implements Func1<GankResponse<T>, T> {

    @Override
    public T call(GankResponse<T> response) {
        if (response.isError()) {
            throw new RuntimeException("获取干货失败");
        }
        return response.getResults();
    }
}