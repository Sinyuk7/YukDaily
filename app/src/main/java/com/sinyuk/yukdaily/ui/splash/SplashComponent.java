package com.sinyuk.yukdaily.ui.splash;

import com.sinyuk.yukdaily.utils.dagger.PerActivity;

import dagger.Subcomponent;

/**
 * Created by Sinyuk on 16/8/19.
 */
@PerActivity
@Subcomponent(
        modules = SplashModule.class
)
public interface SplashComponent {

}
