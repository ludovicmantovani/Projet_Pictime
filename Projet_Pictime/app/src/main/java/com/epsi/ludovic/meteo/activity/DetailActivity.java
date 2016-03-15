package com.epsi.ludovic.meteo.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.epsi.ludovic.meteo.DAO.CityDAO;
import com.epsi.ludovic.meteo.R;
import com.epsi.ludovic.meteo.itf.Weather;
import com.epsi.ludovic.meteo.object.City;
import com.epsi.ludovic.meteo.object.DataSearch;
import com.epsi.ludovic.meteo.service.ServiceGenerator;

import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class DetailActivity extends MenuActivity implements SensorEventListener {

    private final Context context = this;
    private Map<String, String> parameters = null;
    private SensorManager sensorManager;
    private CityDAO cityDao = new CityDAO(this);
    private TextView lblCityName;
    private TextView lblWeather;
    private TextView lblWind;
    private TextView lblTemp;
    private TextView lblPressure;
    private TextView lblHumidity;
    private TextView lblUpdate;
    private ImageView iconeWeather;
    private ImageView iconeFavorite;
    private Weather weatherService = null;
    private long lastUpdate;
    private String idCity;
    private City data;
    ImageView.OnClickListener switchStarHandler = new ImageView.OnClickListener() {
        @Override
        public void onClick(View v) {
            cityDao.open();
            if (cityDao.getCityById(idCity).isFavorite()) {
                cityDao.setToFavorite(data, false);
                iconeFavorite.setImageResource(R.drawable.ic_star_false);
            } else {
                cityDao.setToFavorite(data, true);
                iconeFavorite.setImageResource(R.drawable.ic_star);
            }
            cityDao.close();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        lblCityName = (TextView) findViewById(R.id.city_name);
        lblWeather = (TextView) findViewById(R.id.city_weather);
        lblWind = (TextView) findViewById(R.id.city_wind);
        lblTemp = (TextView) findViewById(R.id.city_temp);
        lblPressure = (TextView) findViewById(R.id.city_pressure);
        lblHumidity = (TextView) findViewById(R.id.city_humidity);
        lblUpdate = (TextView) findViewById(R.id.city_update);
        iconeWeather = (ImageView) findViewById(R.id.city_logo);
        iconeFavorite = (ImageView) findViewById(R.id.city_favorite);

        weatherService = ServiceGenerator.createService(Weather.class);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        data = (City) bundle.getSerializable("data");
        idCity = data.getId();
        parameters = new LinkedHashMap<String, String>();
        parameters.put("id", idCity);
        parameters.put("lang", "fr");
        parameters.put("units", "metric");
        parameters.put("APPID", "dea3ec44f7bd6dbcdbd20c4bbf9b6f05");
        iconeFavorite.setOnClickListener(switchStarHandler);

        updateInformation();


    }

    private void updateInformation() {
        try {
            retrofit.Callback<City> c = new retrofit.Callback<City>() {

                @Override
                public void success(City s, Response response) {

                    cityDao.open();
                    cityDao.update(s);
                    City city = cityDao.getCityById(idCity);
                    cityDao.close();
                    lblCityName.setText(city.getName());
                    lblWeather.setText(city.getWeather().get(0).getDescription().toString().toUpperCase());
                    Double speed = Double.parseDouble(city.getWind().getSpeed()) * (3.6);
                    lblWind.setText("Vent : " + String.format("%.0f", speed) + " km/h");
                    lblTemp.setText(city.getMain().getTemp() + " °C");
                    lblPressure.setText("Pression Atmosphérique : " + city.getMain().getPressure() + " hPa");
                    lblHumidity.setText("Taux d'humidité : " + city.getMain().getHumidity() + " %");
                    DateFormat shortDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
                    lblUpdate.setText(shortDateFormat.format(city.getUpdate()));
                    String name = "logo" + city.getWeather().get(0).getIcon();
                    int id = context.getResources().getIdentifier(name, "drawable",
                            context.getPackageName());
                    iconeWeather.setImageResource(id);
                    if (city.isFavorite()) {
                        iconeFavorite.setImageResource(R.drawable.ic_star);
                    }


                }

                @Override
                public void failure(RetrofitError error) {
                    cityDao.open();
                    City city = cityDao.getCityById(idCity);
                    cityDao.close();
                    lblCityName.setText(city.getName());
                    lblWeather.setText(city.getWeather().get(0).getDescription().toString().toUpperCase());
                    Double speed = Double.parseDouble(city.getWind().getSpeed()) * (3.6);
                    lblWind.setText("Vent : " + String.format("%.0f", speed) + " km/h");
                    lblTemp.setText(city.getMain().getTemp() + " °C");
                    lblPressure.setText("Pression Atmosphérique : " + city.getMain().getPressure() + " hPa");
                    lblHumidity.setText("Taux d'humidité : " + city.getMain().getHumidity() + " %");
                    DateFormat shortDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
                    lblUpdate.setText(shortDateFormat.format(city.getUpdate()));
                    String name = "logo" + city.getWeather().get(0).getIcon();
                    int id = context.getResources().getIdentifier(name, "drawable",
                            context.getPackageName());
                    iconeWeather.setImageResource(id);
                    if (city.isFavorite()) {
                        iconeFavorite.setImageResource(R.drawable.ic_star);
                    }


                }
            };
            weatherService.searchWeather(parameters, c);
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        // register this class as a listener for the orientation and
        // accelerometer sensors
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = event.timestamp;
        if (accelationSquareRoot >= 2) //
        {
            if (actualTime - lastUpdate < 200) {
                return;
            }
            lastUpdate = actualTime;
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm.getActiveNetworkInfo() != null) {
                updateInformation();
                Toast.makeText(context, "La météo a été mise à jour ",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Aucune connexion internet : la météo de la ville ne peut être mise à jour ",
                        Toast.LENGTH_LONG).show();
            }

        }
    }

}


