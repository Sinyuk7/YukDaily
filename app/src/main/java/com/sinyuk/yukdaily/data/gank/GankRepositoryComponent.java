package com.sinyuk.yukdaily.data.gank;

import com.sinyuk.yukdaily.ui.gank.GankFragment;

import javax.inject.Singleton;

import dagger.Subcomponent;

/**
 * Created by Sinyuk on 16.10.25.
 */
@Singleton
@Subcomponent(
        modules = {
                GankRepositoryModule.class
        }
)
public interface GankRepositoryComponent {
    void inject(GankFragment target);
}


