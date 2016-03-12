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

import com.epsi.ludovic.meteo.R;


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
                Log.d("Test", "3");
                switch (menuItem.getItemId()) {

                    case R.id.nav_home:
                        Intent home = new Intent(getApplicationContext(), SearchActivity.class);
                        startActivity(home);
                        break;
                    case R.id.nav_star:
                        Intent star = new Intent(getApplicationContext(), ListActivity.class);
                        startActivity(star);
                        break;
                    case R.id.nav_maps:
                        Intent map = new Intent(getApplicationContext(), MapActivity.class);
                        startActivity(map);
                        break;
                    case R.id.nav_contact:
                        /*TODO a changer*/
                        Intent contact = new Intent(getApplicationContext(), SearchActivity.class);
                        startActivity(contact);
                        break;

                }
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }

        });

    }


}








