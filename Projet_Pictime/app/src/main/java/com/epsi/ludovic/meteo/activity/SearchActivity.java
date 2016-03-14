package com.epsi.ludovic.meteo.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.epsi.ludovic.meteo.R;
import com.epsi.ludovic.meteo.itf.Weather;
import com.epsi.ludovic.meteo.object.City;
import com.epsi.ludovic.meteo.object.DataSearch;
import com.epsi.ludovic.meteo.service.ServiceGenerator;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchActivity extends MenuActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    //TODO delete distance search
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private final Context context = this;
    private EditText etVille = null;
    private TextView tvDistance = null;
    private SeekBar skbDistance = null;
    private int seekBarValue = 50;
    private Button btnRechercher = null;
    private Button btnFavoris = null;
    private Button btnFavorisMap = null;
    private GoogleApiClient mGoogleApiClient;
    private Weather weatherService = null;
    private Map<String, String> parameters = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);
        buildGoogleApiClient();
           /* mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();*/

        etVille = (EditText) findViewById(R.id.eTVille);
        btnRechercher = (Button) findViewById(R.id.btnRechercher);
        btnFavoris = (Button) findViewById(R.id.btnFavoris);
        btnFavorisMap = (Button) findViewById(R.id.btnFavorisMap);
        weatherService = ServiceGenerator.createService(Weather.class);

        if (etVille == null || btnRechercher == null ||
                btnFavoris == null || btnFavorisMap == null) {
            throw new NullPointerException("Widget not found in view !");
        }

        btnRechercher.setOnClickListener(searchDistanceHandler);
        btnFavoris.setOnClickListener(searchFavorisHandler);
        btnFavorisMap.setOnClickListener(searchFavorisMapHandler);

    }


    View.OnClickListener searchDistanceHandler = new View.OnClickListener() {
        public void onClick(View v) {
            displayLocation();
            /**
             * Method to display the location on UI
             * */

/*        Intent i = new Intent(SearchActivity.this, ListActivity.class);

        startActivity(i);*/
    }
};



    View.OnClickListener searchFavorisHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Appel de l'activité list pour les favoris
            Intent i = new Intent(SearchActivity.this, ListActivity.class);
            startActivity(i);
        }
    };

    View.OnClickListener searchFavorisMapHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Appel de l'activité Map pour les favoris
            Intent i = new Intent(SearchActivity.this, MapActivity.class);
            startActivity(i);
        }
    };


    @Override
    public void onResume() {
        super.onResume();

        checkPlayServices();
    }

    @Override
    public void onStart() {

        super.onStart();
/*        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }*/
    }


    /**
     * Method to verify google play services on the device
     * */
    public boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode,getParent() ,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Vous n'êtes pas pourvu d'un téléphone compatible", Toast.LENGTH_LONG)
                        .show();
            }
            return false;

        }
        return true;
    }

    public void displayLocation() {

        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: DEMANDER L'AUTORISATION
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;

        }

        mGoogleApiClient.connect();
        Location mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
             Integer number = 10;

            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            parameters = new LinkedHashMap<String, String>();
            parameters.put("lat", String.valueOf(latitude));
            parameters.put("lon", String.valueOf(longitude));
            parameters.put("cnt",  String.valueOf(number));
            parameters.put("APPID", "dea3ec44f7bd6dbcdbd20c4bbf9b6f05");
        }


        try {
            retrofit.Callback<JsonElement> c = new retrofit.Callback<JsonElement>() {

                @Override
                public void success(JsonElement s, Response response) {
                    Gson gson = new Gson();
                    DataSearch data = gson.fromJson(s, DataSearch.class);
                    Intent intent = new Intent(SearchActivity.this, ListActivity.class);
                    intent.putExtra("data", data);
                    startActivity(intent);

                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("DEBUG1", error.getUrl());
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle(error.getMessage())
                            .setMessage("Failed to " + error.getUrl() + ": " +
                                    error.getMessage())
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert);
                    alert.show();
                }
            };
            weatherService.searchGPS(parameters, c);
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }


    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        if (checkPlayServices()) {
            mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
        }    }

    @Override
    public void onConnected(Bundle bundle) {
      displayLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(),
                "La connexion avec un élément essentiel pour la localisation n'est pas possible", Toast.LENGTH_LONG)
                .show();

    }
}
