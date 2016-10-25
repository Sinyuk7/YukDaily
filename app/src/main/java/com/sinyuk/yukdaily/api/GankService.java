package com.sinyuk.yukdaily.api;

import com.sinyuk.yukdaily.entity.Gank.GankResponse;
import com.sinyuk.yukdaily.entity.Gank.GankResult;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Sinyuk on 16.10.20.
 */

public interface GankService {


    @GET("/day/{year}/{month}/{day}")
    Observable<GankResponse<GankResult>> getGankToday(
            @Path("year") int year,
            @Path("month") int month,
            @Path("day") int day);


    @GET("day/history")
    Observable<GankResponse<List<String>>> getPsotedDates();


}
