package com.sinyuk.yukdaily.api;

import com.sinyuk.yukdaily.entity.Gank.GankResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Sinyuk on 16.10.20.
 */

public interface GankService {


    @GET("/day/{year}/{month}/{day}")
    Observable<GankResponse> getGankToday(
            @Path("year") int year,
            @Path("month") int month,
            @Path("day") int day);


}
