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
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.epsi.ludovic.meteo.R;
import com.epsi.ludovic.meteo.itf.Weather;
import com.epsi.ludovic.meteo.object.City;
import com.epsi.ludovic.meteo.service.ServiceGenerator;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.HashMap;
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
    private Weather city = null;
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
        tvDistance = (TextView) findViewById(R.id.tvDistance);
        skbDistance = (SeekBar) findViewById(R.id.skbDistance);
        btnRechercher = (Button) findViewById(R.id.btnRechercher);
        btnFavoris = (Button) findViewById(R.id.btnFavoris);
        btnFavorisMap = (Button) findViewById(R.id.btnFavorisMap);
        city = ServiceGenerator.createService(Weather.class);

        if (etVille == null || tvDistance == null || skbDistance == null ||
                btnRechercher == null || btnFavoris == null || btnFavorisMap == null) {
            throw new NullPointerException("Widget not found in view !");
        }

        tvDistance.setText(seekBarValue + " Km");
        skbDistance.setProgress(seekBarValue);
        skbDistance.setOnSeekBarChangeListener(distanceHandler);
        btnRechercher.setOnClickListener(searchDistanceHandler);
        btnFavoris.setOnClickListener(searchFavorisHandler);
        btnFavorisMap.setOnClickListener(searchFavorisMapHandler);

    }


    SeekBar.OnSeekBarChangeListener distanceHandler = new SeekBar.OnSeekBarChangeListener(){
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            seekBarValue = progress;
            tvDistance.setText(seekBarValue + " Km");
        }
    };

    View.OnClickListener searchDistanceHandler = new View.OnClickListener() {
        public void onClick(View v) {
            displayLocation();
            /**
             * Method to display the location on UI
             * */

        Intent i = new Intent(SearchActivity.this, ListActivity.class);

        startActivity(i);
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

            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            Log.d("DisplayLOCATION", String.valueOf(latitude) + String.valueOf(longitude));
        }
        /*String ville = SearchActivity.this.etVille.getText().toString();
        if (ville != "") {
            parameters = new HashMap<String, String>();
            parameters.put("q", ville);
            parameters.put("lang", "fr");
            parameters.put("units", "metric");
            parameters.put("appid", "44db6a862fba0b067b1930da0d769e98");

            try {
                retrofit.Callback<City> c = new retrofit.Callback<City>() {

                    @Override
                    public void success(City s, Response response) {
                        Log.d("SUCESS", s.toString());
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
                city.citys(parameters, c);
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, "Ville manquante !", Toast.LENGTH_LONG).show();
        }*/
        //Appel de l'activité list pour les favoris
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

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(),
                "La connexion avec un élément essentiel pour la localisation n'est pas possible", Toast.LENGTH_LONG)
                .show();

    }
}
