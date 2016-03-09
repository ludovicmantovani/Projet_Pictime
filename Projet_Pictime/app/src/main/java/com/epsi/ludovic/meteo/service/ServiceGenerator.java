package com.epsi.ludovic.meteo.service;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by Ludovic on 24/01/2016.
 */
public class ServiceGenerator {
    public static final String API_BASE_URL = "http://api.openweathermap.org/data/2.5";

    private static RestAdapter.Builder builder = new RestAdapter.Builder()
            .setEndpoint(API_BASE_URL)
            .setClient(new OkClient(new OkHttpClient()));

    public static <S> S createService(Class<S> serviceClass) {
        RestAdapter adapter = builder.build();
        return adapter.create(serviceClass);
    }

}
