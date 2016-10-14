package com.sinyuk.yukdaily;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.sinyuk.yukdaily.api.ApiModule;

/**
 * Created by Sinyuk on 2016/10/12.
 */

public class App extends Application {


    public static final String TAG = "App";
    private AppComponent appComponent = null;

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .apiModule(new ApiModule())
                .appModule(new AppModule(this)).build();
    }

    public AppComponent getAppComponent() {
        if (appComponent != null) {
            Log.d(TAG, "getAppComponent: not null");
        }
        return appComponent;
    }
}
