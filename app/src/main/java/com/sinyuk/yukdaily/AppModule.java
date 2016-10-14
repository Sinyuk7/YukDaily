package com.sinyuk.yukdaily;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.sinyuk.myutils.system.ToastUtils;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Sinyuk on 2016/10/12.
 */
@Module
final class AppModule {
    private Application application;

    AppModule(Application application) {
        this.application = application;
    }

    @Singleton
    @Provides
    Application provideApplication() {
        return application;
    }

    @Singleton
    @Provides
    Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {
        return application.getSharedPreferences("bihu_daily", MODE_PRIVATE);
    }

    @Provides
    @Singleton
    RxSharedPreferences providePreferences(SharedPreferences preferences) {
        return RxSharedPreferences.create(preferences);
    }

    @Provides
    @Singleton
    File provideCachePath() {
        return new File(application.getExternalCacheDir(), "network_cache");
    }

    @Provides
    @Singleton
    public ToastUtils provideToastUtils() {
        return new ToastUtils(application);
    }
}
