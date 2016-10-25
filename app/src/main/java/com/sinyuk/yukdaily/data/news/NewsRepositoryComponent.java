package com.sinyuk.yukdaily.data.news;

import com.sinyuk.yukdaily.ui.news.BrowserActivity;
import com.sinyuk.yukdaily.ui.news.NewsCommentFragment;
import com.sinyuk.yukdaily.ui.news.NewsFragment;
import com.sinyuk.yukdaily.ui.splash.SplashActivity;

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

    void inject(BrowserActivity target);

    void inject(NewsCommentFragment target);

}