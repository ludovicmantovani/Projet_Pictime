package com.epsi.ludovic.meteo.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.epsi.ludovic.meteo.R;
import com.epsi.ludovic.meteo.itf.Weather;
import com.epsi.ludovic.meteo.object.City;
import com.epsi.ludovic.meteo.service.ServiceGenerator;

import java.util.HashMap;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchActivity extends AppCompatActivity {

    private final Context context = this;
    private EditText finding = null;
    private Button b1 = null;
    private TextView info = null;
    private Weather city = null;
    private Map<String, String> parameters = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setContentView(R.layout.activity_search);
        finding = (EditText) findViewById(R.id.eTVille);
        info = (TextView) findViewById(R.id.info);
        b1 = (Button) findViewById(R.id.buttonrechercher);
        city = ServiceGenerator.createService(Weather.class);

        if (finding == null || info == null || b1 == null) {
            throw new NullPointerException("Widget not found in view !");
        }

        b1.setOnClickListener(searchHandler);
    }

    View.OnClickListener searchHandler = new View.OnClickListener() {
        public void onClick(View v) {
            String ville = finding.getText().toString();
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
                            Log.d("SUCESS1", s.toString());
                            info.setText(s.toString());
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
                            info.setText(error.getMessage());
                        }
                    };
                    city.citys(parameters, c);
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(context, "Ville manquante !", Toast.LENGTH_LONG).show();
            }
        }
    };
}
