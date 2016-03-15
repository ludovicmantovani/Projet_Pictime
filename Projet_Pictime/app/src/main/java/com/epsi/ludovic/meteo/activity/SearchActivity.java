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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.epsi.ludovic.meteo.DAO.CityDAO;
import com.epsi.ludovic.meteo.R;
import com.epsi.ludovic.meteo.adapter.AutocompleteAdapter;
import com.epsi.ludovic.meteo.adapter.ListAdapter;
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
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchActivity extends MenuActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private final Context context = this;
    private AutoCompleteTextView autoCompleteTextViewVille = null;
    private Button btnRechercher = null;
    private Button btnFavoris = null;
    private Button btnFavorisMap = null;
    private GoogleApiClient mGoogleApiClient;
    private Weather weatherService = null;
    private Map<String, String> parameters = null;
    ListAdapter listAdapter = null;
    CityDAO cityDAO = null;

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

        autoCompleteTextViewVille = (AutoCompleteTextView) findViewById(R.id.aCTVVille);
        btnRechercher = (Button) findViewById(R.id.btnRechercher);
        btnFavoris = (Button) findViewById(R.id.btnFavoris);
        btnFavorisMap = (Button) findViewById(R.id.btnFavorisMap);
        weatherService = ServiceGenerator.createService(Weather.class);

        if (autoCompleteTextViewVille == null || btnRechercher == null ||
                btnFavoris == null || btnFavorisMap == null) {
            throw new NullPointerException("Widget not found in view !");
        }

        cityDAO = new CityDAO(context);
        cityDAO.open();
        AutocompleteAdapter autocompleteAdapter = new AutocompleteAdapter(context, R.layout.activity_search, R.id.city_name);
        cityDAO.close();
        autoCompleteTextViewVille.setAdapter(autocompleteAdapter);

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

    private void goToFavorite(Class intentClass)
    {
        DataSearch dataSearch = null;

        //Recherche des villes favorites dans la BDD
        cityDAO.open();
        dataSearch = cityDAO.getFavorite();
        cityDAO.close();

        //Si favoris présents dans la BDD
        if (dataSearch.getCities().size() > 0) {
            //Appel de l'activité list pour les favoris
            Intent i = new Intent(SearchActivity.this, intentClass);
            i.putExtra("data", dataSearch);
            startActivity(i);
        }
        else //sinon affichage message
        {
            Toast.makeText(context, "Vous n'avez pas de villes favorites.",
                    Toast.LENGTH_LONG).show();
        }
    }

    View.OnClickListener searchFavorisHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            goToFavorite(ListActivity.class);
        }
    };

    View.OnClickListener searchFavorisMapHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            goToFavorite(MapActivity.class);
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
     */
    public boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getParent(),
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

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
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
            parameters.put("cnt", String.valueOf(number));
            parameters.put("APPID", "dea3ec44f7bd6dbcdbd20c4bbf9b6f05");


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
                        Toast.makeText(getApplicationContext(),
                                "Attention, impossible de récuperer les villes aux alentours. Vérifiez votre connexion internet (2G/3G/4G ou Wifi)", Toast.LENGTH_LONG)
                                .show();
                    }
                };
                weatherService.searchGPS(parameters, c);
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
    }


    /**
     * Creating google api client object
     */
    protected synchronized void buildGoogleApiClient() {
        if (checkPlayServices()) {
            mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
        }
    }

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
