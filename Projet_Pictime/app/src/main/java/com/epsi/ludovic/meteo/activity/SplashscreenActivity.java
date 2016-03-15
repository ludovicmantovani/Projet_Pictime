package com.epsi.ludovic.meteo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.epsi.ludovic.meteo.DAO.BddNameConvention;
import com.epsi.ludovic.meteo.DAO.CityDAO;
import com.epsi.ludovic.meteo.DAO.DAOBase;
import com.epsi.ludovic.meteo.R;
import com.epsi.ludovic.meteo.object.Cities;
import com.epsi.ludovic.meteo.object.City;
import com.epsi.ludovic.meteo.object.DataSearch;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SplashscreenActivity extends AppCompatActivity {

    //Timer pour le temps d'attente
    private static int TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        Context context = getApplicationContext();
        InputStream jsonFile = null;
        String jsonContent = null;
        Gson gson = new Gson();
        Cities cities = null;
        CityDAO cityDAO = null;


        File database = context.getDatabasePath(BddNameConvention.DATABASE_NAME);

        if (!database.exists()) {
            try {
                jsonFile = getResources().openRawResource(R.raw.cities);
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }

            if (jsonFile != null) {
                long id = 1;
                jsonContent = convertStreamToString(jsonFile);
                cities = gson.fromJson(jsonContent, Cities.class);
                cityDAO = new CityDAO(context);
                cityDAO.open();


                for (City city : cities.getCities())
                {
                    //city.setTechnical_id(id);
                    cityDAO.add(city);
                    id++;
                }
                cityDAO.close();

            }
        }
        //execution de l'attente
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Appel de l'activité suivante
                Intent i = new Intent(SplashscreenActivity.this, SearchActivity.class);
                startActivity(i);
                //Au retour, arret sans passer par le splashscreen
                finish();
            }
        }, TIME);
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
