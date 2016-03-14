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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
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

public class DetailActivity extends MenuActivity implements SensorEventListener {

    private Map<String, String> parameters = null;
    private SensorManager sensorManager;
    private final Context context = this;
    private TextView lblCityName;
    private TextView lblWeather;
    private TextView lblWind;
    private TextView lblTemp;
    private TextView lblPressure;
    private TextView lblHumidity;
    private ImageView iconeWeather;
    private  Weather weatherService = null;
    private long lastUpdate;

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
        iconeWeather = (ImageView) findViewById(R.id.city_logo);

        weatherService = ServiceGenerator.createService(Weather.class);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        City data =  (City) bundle.getSerializable("data");

        parameters = new LinkedHashMap<String, String>();
        parameters.put("id", data.getId());
        parameters.put("lang", "fr");
        parameters.put("units", "metric");
        parameters.put("APPID", "dea3ec44f7bd6dbcdbd20c4bbf9b6f05");
        displayInformation();
    }


        public void displayInformation()
    {
        try {
            retrofit.Callback<City> c = new retrofit.Callback<City>() {

                @Override
                public void success(City s, Response response) {
                    lblCityName.setText(s.getName());
                    lblWeather.setText(s.getWeather().get(0).getDescription().toString().toUpperCase());
                    Double speed = Double.parseDouble(s.getWind().getSpeed())*(3.6);
                    lblWind.setText("Vent : " +String.format("%.0f", speed) + " km/h");
                    lblTemp.setText(s.getMain().getTemp() + " °C");
                    lblPressure.setText("Pression Atmosphérique : " + s.getMain().getPressure()+ "hPa");
                    lblHumidity.setText("Taux d'humidité : "+ s.getMain().getHumidity() +" %");
                    String name = "logo"+s.getWeather().get(0).getIcon();
                    int id = context.getResources().getIdentifier(name, "drawable",
                            context.getPackageName());
                    iconeWeather.setImageResource(id);

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
            displayInformation();
        }
    }

}
