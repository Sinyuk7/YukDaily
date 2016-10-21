package com.sinyuk.yukdaily;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.sinyuk.yukdaily.api.ApiModule;
import com.squareup.leakcanary.LeakCanary;

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
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
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
