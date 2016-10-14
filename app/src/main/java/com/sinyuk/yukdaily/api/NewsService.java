package com.sinyuk.yukdaily.api;

import com.sinyuk.yukdaily.model.LatestNews;
import com.sinyuk.yukdaily.model.StartImage;

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
}
