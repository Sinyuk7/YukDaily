package com.sinyuk.yukdaily.data.news;

import com.sinyuk.yukdaily.ui.news.NewsFragment;

import javax.inject.Singleton;

import dagger.Subcomponent;

/**
 * Created by Sinyuk on 16.10.21.
 */
@Singleton
@Subcomponent(
        modules = {
                NewsRepositoryModule.class
        }
)
public interface NewsRepositoryComponent {
    void inject(NewsFragment target);
}