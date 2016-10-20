package com.sinyuk.yukdaily.data.news;

import android.app.Application;

import com.sinyuk.yukdaily.api.NewsService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Sinyuk on 16.10.21.
 */
@Module
public class NewsRepositoryModule {
    @Provides
    @Singleton
    public NewsRepository provideNewsRepository(Application context, NewsService newsService) {
        return new NewsRepository(context, newsService);
    }
}
