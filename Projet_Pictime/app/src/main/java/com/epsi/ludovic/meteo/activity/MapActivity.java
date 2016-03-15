package com.epsi.ludovic.meteo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.epsi.ludovic.meteo.R;
import com.epsi.ludovic.meteo.object.City;
import com.epsi.ludovic.meteo.object.Coord;
import com.epsi.ludovic.meteo.object.DataSearch;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends MenuActivity {


    private GoogleMap googleMap;

    private MapView mapView;


    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        DataSearch data =  (DataSearch) bundle.getSerializable("data");
        ArrayList<City> list = data.getCities();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        try {
            if (googleMap == null) {
                mapView = (MapView) findViewById(R.id.map);
                mapView.onCreate(savedInstanceState);
                googleMap = mapView.getMap();
            }
            MapsInitializer.initialize(getApplicationContext());
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            for(City city :  list) {
                googleMap.addMarker(new MarkerOptions().
                        position(new LatLng(Double.parseDouble(city.getCoord().getLat()), Double.parseDouble(city.getCoord().getLon()))).title(city.getName()));
            }
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(list.get(0).getCoord().getLat()), Double.parseDouble(list.get(0).getCoord().getLon())), 10);
            googleMap.animateCamera(cameraUpdate);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
