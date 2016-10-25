package com.sinyuk.yukdaily.utils.rx;

import com.sinyuk.yukdaily.entity.Gank.GankResponse;
import com.sinyuk.yukdaily.entity.Gank.GankResult;

import java.util.List;

import rx.functions.Func1;

/**
 * 用来统一处理gank回调中的error
 */
public final class GankResponseFunc implements Func1<GankResponse, List<GankResult>> {

    @Override
    public List<GankResult> call(GankResponse response) {
        if (response.isError()) {
            throw new RuntimeException("获取干货失败");
        }
        return response.getResults();
    }
}