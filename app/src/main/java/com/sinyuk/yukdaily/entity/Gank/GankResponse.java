package com.sinyuk.yukdaily.entity.Gank;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Sinyuk on 16.10.20.
 */

public class GankResponse<T>{
    @SerializedName("error")
    private boolean isError;
    @SerializedName("results")
    private T results;

    public boolean isError() { return isError;}

    public T getResults() { return results;}
}
