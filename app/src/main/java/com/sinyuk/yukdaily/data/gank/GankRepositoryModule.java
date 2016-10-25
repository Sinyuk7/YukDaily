package com.sinyuk.yukdaily.data.gank;

import com.sinyuk.yukdaily.api.GankService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Sinyuk on 16.10.25.
 */
@Module
public class GankRepositoryModule {
    @Provides
    @Singleton
    public GankRepository provideGankRepository(GankService gankService) {
        return new GankRepository(gankService);
    }
}
