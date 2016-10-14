package com.sinyuk.yukdaily.api;

import com.f2prateek.rx.preferences.Preference;
import com.sinyuk.yukdaily.utils.dagger.Token;

import java.io.IOException;

import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


@Singleton
public final class OauthInterceptor implements Interceptor {
    private Preference<String> mAccessToken;

    public OauthInterceptor(@Token Preference<String> accessToken) {
        this.mAccessToken = accessToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        if (mAccessToken.isSet()) {
//            builder.header("Authorization", DribbleApi.ACCESS_TYPE + " " + mAccessToken.get());
        } else {
//            builder.header("Authorization", DribbleApi.ACCESS_TYPE + " " + BuildConfig.DRIBBBLE_CLIENT_ACCESS_TOKEN);
        }

        return chain.proceed(builder.build());
    }
}
