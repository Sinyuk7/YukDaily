package com.sinyuk.yukdaily;

import com.sinyuk.yukdaily.api.ApiModule;
import com.sinyuk.yukdaily.ui.news.NewsFragment;
import com.sinyuk.yukdaily.ui.splash.SplashComponent;
import com.sinyuk.yukdaily.ui.splash.SplashModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Sinyuk on 16/6/30.
 */
@Singleton
@Component(modules = {AppModule.class, ApiModule.class})
public interface AppComponent {
    SplashComponent plus(SplashModule module);

    void inject(NewsFragment target);
}
