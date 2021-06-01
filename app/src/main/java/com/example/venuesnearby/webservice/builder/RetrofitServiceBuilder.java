package com.example.venuesnearby.webservice.builder;

import android.util.Log;

import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServiceBuilder {

    private static final String TAG = RetrofitServiceBuilder.class.getSimpleName();
    private static final String FOURSQUARE_BASE_URL = "https://api.foursquare.com/v2/";

    private static OkHttpClient.Builder sOkHttpClientBuilder;

    static {
        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> Log.d(TAG, message))
                .setLevel(HttpLoggingInterceptor.Level.BODY);

        sOkHttpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor);
    }

    public static <S> S buildService(Class<S> serviceType) {
        return new Retrofit.Builder()
                .baseUrl(FOURSQUARE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(sOkHttpClientBuilder.build())
                .build()
                .create(serviceType);
    }
}

