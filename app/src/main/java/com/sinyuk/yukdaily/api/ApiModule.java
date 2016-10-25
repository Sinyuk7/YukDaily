package com.sinyuk.yukdaily.api;

import android.app.Application;

import com.f2prateek.rx.preferences.Preference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sinyuk.yukdaily.BuildConfig;
import com.sinyuk.yukdaily.utils.dagger.Token;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sinyuk on 2016/10/12.
 */
@Module
public class ApiModule {
    private static final long MAX_OKHTTP_CACHE = 1024 * 1024 * 50;

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                // Blank fields are included as null instead of being omitted.
                .serializeNulls()
                .create();
    }

    @Provides
    @Singleton
    OauthInterceptor provideOauthInterceptor(@Token Preference<String> accessToken) {
        return new OauthInterceptor(accessToken);
    }


    @Provides
    @Singleton
    @Named("Cached")
    public OkHttpClient provideOkHttpClient(Application application, File cacheFile) {

        Cache cache = new Cache(cacheFile, MAX_OKHTTP_CACHE);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }

//        final Interceptor REWRITE_RESPONSE_INTERCEPTOR = chain -> {
//            Response originalResponse = chain.proceed(chain.request());
//            String cacheControl = originalResponse.header("Cache-Control");
//            if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
//                    cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {
//                return originalResponse.newBuilder()
//                        .header("Cache-Control", "public, max-age=" + 0)
//                        .build();
//            } else {
//                return originalResponse;
//            }
//        };
//
//        final Interceptor OFFLINE_INTERCEPTOR = chain -> {
//            Request request = chain.request();
//
//            if (!NetWorkUtils.isNetworkConnection(application)) {
//                int maxStale = 60 * 60 * 24 * 3;
//                request = request.newBuilder()
//                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
//                        .build();
//            }
//            return chain.proceed(request);
//        };
//
//        builder.cache(cache)
//                .addNetworkInterceptor(REWRITE_RESPONSE_INTERCEPTOR)
//                .addInterceptor(OFFLINE_INTERCEPTOR);

        //设置超时
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);

        return builder.build();
    }

    @Provides
    @Singleton
    @Named("News")
    Retrofit provideNewsRetrofit(Gson gson, @Named("Cached") OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(NewsApi.END_POINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }


    @Provides
    @Singleton
    @Named("Gank")
    Retrofit provideGankRetrofit(Gson gson, @Named("Cached") OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(GankApi.END_POINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }


    @Provides
    @Singleton
    NewsService provideNewsService(@Named("News") Retrofit retrofit) {
        return retrofit.create(NewsService.class);
    }

    @Provides
    @Singleton
    GankService provideGankService(@Named("Gank") Retrofit retrofit) {
        return retrofit.create(GankService.class);
    }
}
