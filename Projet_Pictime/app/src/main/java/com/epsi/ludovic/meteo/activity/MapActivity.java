package com.epsi.ludovic.meteo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.epsi.ludovic.meteo.R;
import com.epsi.ludovic.meteo.object.City;
import com.epsi.ludovic.meteo.object.Coord;
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

    List<City> test = new ArrayList<City>();
    City cit1 = new City();
    City cit2 = new City();
    ArrayList<LatLng> points = new ArrayList<LatLng>();
    public final LatLng tutorialsPoint = new LatLng(50.6567115 , 3.0788290999999997);
    public final LatLng tutorialsPoint2 = new LatLng(50.66667175292969 , 3.083329916000366);

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
        //TODO liste de villes à récupérer dans la bdd
        cit1.setName("Marcq en Baroeul");
        cit1.setCoord(new Coord());
        cit1.getCoord().setLat("50.6567115");
        cit1.getCoord().setLon("3.0788290999999997");
        cit2.setName("La Madeleine");
        cit2.setCoord(new Coord());
        cit2.getCoord().setLat("50.66667175292969");
        cit2.getCoord().setLon("3.083329916000366");

        test.add(cit1);
        test.add(cit2);
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
            for(City city :  test) {
                googleMap.addMarker(new MarkerOptions().
                        position(new LatLng(Double.parseDouble(city.getCoord().getLat()), Double.parseDouble(city.getCoord().getLon()))).title(city.getName()));
            }
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(test.get(0).getCoord().getLat()), Double.parseDouble(test.get(0).getCoord().getLon())), 10);
            googleMap.animateCamera(cameraUpdate);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
