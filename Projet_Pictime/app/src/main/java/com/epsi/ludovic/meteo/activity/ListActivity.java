package com.epsi.ludovic.meteo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.epsi.ludovic.meteo.R;
import com.epsi.ludovic.meteo.adapter.ListAdapter;
import com.epsi.ludovic.meteo.object.City;
import com.epsi.ludovic.meteo.object.DataSearch;

import java.util.ArrayList;

public class ListActivity extends MenuActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        ListView mListView = (ListView) findViewById(R.id.listCity);
        Bundle bundle = getIntent().getExtras();
        DataSearch data =  (DataSearch) bundle.getSerializable("data");
        ArrayList<City> list = data.getCities();
        ListAdapter adapter = new ListAdapter(getApplicationContext(), list ) ;
        mListView.setAdapter(adapter);

    }
}
