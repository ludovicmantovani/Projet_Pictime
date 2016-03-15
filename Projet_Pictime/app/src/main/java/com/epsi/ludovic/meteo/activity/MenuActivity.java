package com.epsi.ludovic.meteo.activity;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.epsi.ludovic.meteo.DAO.CityDAO;
import com.epsi.ludovic.meteo.R;
import com.epsi.ludovic.meteo.object.DataSearch;


public class MenuActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView view;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void setContentView(int layoutResID) {
        DrawerLayout fullView = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_menu, null);
        FrameLayout activityContainer = (FrameLayout) fullView.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullView);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_container);
        view = (NavigationView) findViewById(R.id.navigationView);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.nav_home:
                        Intent home = new Intent(getApplicationContext(), SearchActivity.class);
                        startActivity(home);
                        break;
                    case R.id.nav_star:
                        Intent star = new Intent(getApplicationContext(), ListActivity.class);
                        DataSearch dataSearch = null;

                        //Recherche des villes favorites dans la BDD
                        CityDAO cityDAO = new CityDAO(getApplicationContext());
                        cityDAO.open();
                        dataSearch = cityDAO.getFavorite();
                        cityDAO.close();

                        //Si favoris présents dans la BDD
                        if (dataSearch.getCities().size() > 0) {
                            //Appel de l'activité list pour les favoris
                            Intent i = new Intent(getApplicationContext(), ListActivity.class);
                            i.putExtra("data", dataSearch);
                            startActivity(i);
                        }
                        else //sinon affichage message
                        {
                            Toast.makeText(getApplicationContext(), "Vous n'avez pas de villes favorites.",
                                    Toast.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.nav_maps:
                        //Si favoris présents dans la BDD
                        cityDAO = new CityDAO(getApplicationContext());
                        cityDAO.open();
                        dataSearch = cityDAO.getFavorite();
                        cityDAO.close();

                        if (dataSearch.getCities().size() > 0) {
                            //Appel de l'activité list pour les favoris
                            Intent i = new Intent(getApplicationContext(), MapActivity.class);
                            i.putExtra("data", dataSearch);
                            startActivity(i);
                        }
                        else //sinon affichage message
                        {
                            Toast.makeText(getApplicationContext(), "Vous n'avez pas de villes favorites.",
                                    Toast.LENGTH_LONG).show();
                        }

                }
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }

        });

    }


}








