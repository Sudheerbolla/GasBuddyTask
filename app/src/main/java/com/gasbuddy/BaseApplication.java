package com.gasbuddy;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDexApplication;

import com.gasbuddy.imageloading.AppUtils;
import com.gasbuddy.imageloading.WSUtils;
import com.gasbuddy.utils.wsutils.WSInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseApplication extends MultiDexApplication {

    private static BaseApplication mInstance;
    private static WSInterface wsInterfaceUnsplash, wsInterfaceGoogle;
    private OkHttpClient okHttpClientUnsplash, okHttpClientGoogle;

    @Override
    public void onCreate() {
        super.onCreate();
        initRetrofit();
    }

    public static synchronized BaseApplication getInstance() {
        if (mInstance == null) mInstance = new BaseApplication();
        return mInstance;
    }

    public WSInterface getWsClientListenerUnsplash() {
        return wsInterfaceUnsplash;
    }

    public WSInterface getWsClientListenerGoogle() {
        return wsInterfaceGoogle;
    }

    public void initRetrofit() {

        Interceptor headerInterceptorUnsplash = new Interceptor() {
            @Override
            public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                Request.Builder requestBuilder = chain.request().newBuilder();
                requestBuilder.header("Authorization", "Client-ID " + AppUtils.UNSPLASH_ACCESS_KEY);
                return chain.proceed(requestBuilder.build());
            }
        };

        okHttpClientUnsplash = new OkHttpClient().newBuilder().
                addInterceptor(headerInterceptorUnsplash).
                addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).
                readTimeout(WSUtils.CONNECTION_TIMEOUT, TimeUnit.SECONDS).
                connectTimeout(WSUtils.CONNECTION_TIMEOUT, TimeUnit.SECONDS).
                writeTimeout(WSUtils.CONNECTION_TIMEOUT, TimeUnit.SECONDS).
                build();
        okHttpClientGoogle = new OkHttpClient().newBuilder().
                addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).
                readTimeout(WSUtils.CONNECTION_TIMEOUT, TimeUnit.SECONDS).
                connectTimeout(WSUtils.CONNECTION_TIMEOUT, TimeUnit.SECONDS).
                writeTimeout(WSUtils.CONNECTION_TIMEOUT, TimeUnit.SECONDS).
                build();

        wsInterfaceUnsplash = buildRetrofitClient(WSUtils.BASE_URL_UNSPLASH).create(WSInterface.class);
        wsInterfaceGoogle = buildRetrofitClientGoogle(WSUtils.BASE_URL_GOOGLE).create(WSInterface.class);
    }

    private Retrofit buildRetrofitClient(String baseUrl) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClientUnsplash).build();
    }

    private Retrofit buildRetrofitClientGoogle(String baseUrl) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClientGoogle).build();
    }

}
