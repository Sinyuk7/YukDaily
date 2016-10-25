package com.sinyuk.yukdaily.ui.splash;

import com.sinyuk.yukdaily.data.gank.GankRepositoryModule;
import com.sinyuk.yukdaily.data.news.NewsRepositoryModule;

import javax.inject.Singleton;

import dagger.Subcomponent;

/**
 * Created by Sinyuk on 16/8/19.
 */
@Singleton
@Subcomponent(
        modules = {GankRepositoryModule.class, NewsRepositoryModule.class}
)
public interface SplashComponent {
    void inject(SplashActivity target);
}
