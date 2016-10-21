package com.sinyuk.yukdaily.api;

import com.sinyuk.yukdaily.entity.news.News;
import com.sinyuk.yukdaily.entity.news.Stories;
import com.sinyuk.yukdaily.entity.news.StartImage;

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
    Observable<Stories> getLatestNews();

    @GET("news/before/{date}")
    Observable<Stories> getNewsAt(@Path("date") String date);

    @GET("news/{id}")
    Observable<News> getNews(@Path("id") int id);
}
