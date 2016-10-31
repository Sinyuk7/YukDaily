package com.sinyuk.yukdaily.api;

import com.sinyuk.yukdaily.entity.news.News;
import com.sinyuk.yukdaily.entity.news.NewsCommentResponse;
import com.sinyuk.yukdaily.entity.news.NewsExtras;
import com.sinyuk.yukdaily.entity.news.StartImage;
import com.sinyuk.yukdaily.entity.news.Stories;
import com.sinyuk.yukdaily.entity.news.ThemeResponse;

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

    @GET("story-extra/{id}")
    Observable<NewsExtras> getNewsExtras(@Path("id") int id);

    @GET("story/{id}/long-comments")
    Observable<NewsCommentResponse> getNewsLongComments(@Path("id") int id);

    @GET("story/{id}/short-comments")
    Observable<NewsCommentResponse> getNewsShortComments(@Path("id") int id);

    @GET("themes")
    Observable<ThemeResponse> getThemes();


}
