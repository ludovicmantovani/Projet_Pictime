package com.epsi.ludovic.meteo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.epsi.ludovic.meteo.R;

public class DetailActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }
}
