package com.sinyuk.yukdaily;

import com.sinyuk.yukdaily.api.ApiModule;
import com.sinyuk.yukdaily.data.gank.GankRepositoryComponent;
import com.sinyuk.yukdaily.data.gank.GankRepositoryModule;
import com.sinyuk.yukdaily.data.news.NewsRepositoryComponent;
import com.sinyuk.yukdaily.data.news.NewsRepositoryModule;
import com.sinyuk.yukdaily.ui.browser.WebViewActivity;
import com.sinyuk.yukdaily.ui.home.SlidingMenuFragment;
import com.sinyuk.yukdaily.ui.splash.SplashComponent;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Sinyuk on 16/6/30.
 */
@Singleton
@Component(modules = {AppModule.class, ApiModule.class})
public interface AppComponent {

    SplashComponent plus();

    NewsRepositoryComponent plus(NewsRepositoryModule module);

    GankRepositoryComponent plus(GankRepositoryModule module);

    void inject(WebViewActivity target);

    void inject(SlidingMenuFragment target);
}
