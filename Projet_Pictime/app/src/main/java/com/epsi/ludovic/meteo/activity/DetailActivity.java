package com.epsi.ludovic.meteo.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.epsi.ludovic.meteo.R;
import com.epsi.ludovic.meteo.itf.Weather;
import com.epsi.ludovic.meteo.object.City;
import com.epsi.ludovic.meteo.object.DataSearch;
import com.epsi.ludovic.meteo.service.ServiceGenerator;

import java.util.LinkedHashMap;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class DetailActivity extends MenuActivity {

    private Map<String, String> parameters = null;
    private final Context context = this;
    private TextView lblCityName;
    private TextView lblWeather;
    private TextView lblWind;
    private TextView lblTemp;
    private  Weather weatherService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        lblCityName = (TextView) findViewById(R.id.city_name);
        lblWeather = (TextView) findViewById(R.id.city_weather);
        lblWind = (TextView) findViewById(R.id.city_wind);
        lblTemp = (TextView) findViewById(R.id.city_temp);
        weatherService = ServiceGenerator.createService(Weather.class);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String idCity = bundle.getString("idCity");

        parameters = new LinkedHashMap<String, String>();
        parameters.put("id", idCity);
        parameters.put("lang", "fr");
        parameters.put("units", "metric");
        parameters.put("APPID", "dea3ec44f7bd6dbcdbd20c4bbf9b6f05");

        try {
            retrofit.Callback<City> c = new retrofit.Callback<City>() {

                @Override
                public void success(City s, Response response) {
                    lblCityName.setText(s.getName());
                    lblWeather.setText(s.getWeather().get(0).getDescription().toString().toUpperCase());
                    lblWind.setText(s.getWind().getSpeed());
                    lblTemp.setText(s.getMain().getTemp());


                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("DEBUG1", error.getUrl());
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle(error.getMessage())
                            .setMessage("Failed to " + error.getUrl())
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert);
                    alert.show();

                }
            };
            weatherService.searchWeather(parameters, c);
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
