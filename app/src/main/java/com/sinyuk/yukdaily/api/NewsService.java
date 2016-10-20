package com.sinyuk.yukdaily.api;

import com.sinyuk.yukdaily.entity.LatestNews;
import com.sinyuk.yukdaily.entity.OldNews;
import com.sinyuk.yukdaily.entity.StartImage;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Sinyuk on 2016/10/11.
 */

public interface NewsService {
    @GET("start-image/{resolution}")
    Observable<StartImage> getStartImage(@Path("resolution") String resolution);

    @GET("news/latest")
    Observable<LatestNews> getLatestNews();

    @GET("news/before/{date}")
    Observable<OldNews> getNewsAt(String date);

}
