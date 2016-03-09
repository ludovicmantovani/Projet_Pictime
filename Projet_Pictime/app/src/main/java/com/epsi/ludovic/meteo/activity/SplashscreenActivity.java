package com.epsi.ludovic.meteo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

import com.epsi.ludovic.meteo.R;

public class SplashscreenActivity extends AppCompatActivity {

    //Timer pour le temps d'attente
    private static int TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

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
}
