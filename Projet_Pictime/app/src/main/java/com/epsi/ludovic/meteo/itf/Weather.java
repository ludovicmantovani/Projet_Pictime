package com.epsi.ludovic.meteo.itf;

import com.epsi.ludovic.meteo.object.City;

import java.util.Map;

import retrofit.http.GET;
import retrofit.http.QueryMap;

/**
 * Created by Ludovic on 24/01/2016.
 */
public interface Weather {
    @GET("/weather")
    void citys(@QueryMap Map<String, String> c, retrofit.Callback<City> callback);
}
