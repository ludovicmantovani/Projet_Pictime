package com.epsi.ludovic.meteo.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.epsi.ludovic.meteo.R;
import com.epsi.ludovic.meteo.activity.DetailActivity;
import com.epsi.ludovic.meteo.object.City;

import java.util.ArrayList;

/**
 * Created by Betty on 13/03/2016.
 */
public class ListAdapter extends ArrayAdapter<City> {
    public ListAdapter(Context context, ArrayList<City> cities) {
        super(context, 0, cities);
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        // Get the item data for this position
       final City city = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_list_item, parent, false);
        }
        // Lookup view for data population
        Button cityName = (Button) convertView.findViewById(R.id.cityName);
        // Populate the data into the template view using the data object
        cityName.setText(city.getName());
        // Return the completed view to render on screen

        cityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parent.getContext(), DetailActivity.class);

                //On affecte à l'Intent le Bundle que l'on a créé
                intent.putExtra("idCity", city.getId());
                parent.getContext().startActivity(intent);
            }
        });

        return convertView;
    }

}
