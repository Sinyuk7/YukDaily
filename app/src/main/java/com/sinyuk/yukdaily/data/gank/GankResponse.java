package com.sinyuk.yukdaily.data.gank;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Sinyuk on 16.10.20.
 */

public class GankResponse {
    @SerializedName("error")
    private boolean isError;
    @SerializedName("results")
    private List<GankResult> results;

    public boolean isError() { return isError;}

    public List<GankResult> getResults() { return results;}
}
