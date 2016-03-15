package com.epsi.ludovic.meteo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import com.epsi.ludovic.meteo.DAO.BddNameConvention;
import com.epsi.ludovic.meteo.DAO.CityDAO;
import com.epsi.ludovic.meteo.R;
import com.epsi.ludovic.meteo.activity.DetailActivity;
import com.epsi.ludovic.meteo.object.City;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ludovic on 14/03/2016.
 */
public class AutocompleteAdapter extends ArrayAdapter<City> {

    Context context = getContext();
    int resource, textViewResourceId;
    List<City> tempItems, suggestions;
    CityDAO cityDAO = new CityDAO(context);

    public AutocompleteAdapter(Context context, int resource, int textViewResourceId)
    {
        super(context, resource, textViewResourceId);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;;
        suggestions = new ArrayList<City>();
        tempItems = new ArrayList<City>();
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final City city = getItem(position);
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
                cityDAO.open();
                if (cityDAO.getCityById(city.getId()).getUpdate() != null)
                {
                    Intent intent = new Intent(parent.getContext(), DetailActivity.class);

                    //On affecte à l'Intent le Bundle que l'on a créé
                    intent.putExtra("data", city);
                    parent.getContext().startActivity(intent);
                }

                else {
                    Toast.makeText(parent.getContext(), "Impossible d'afficher la météo de cette ville. Aucune donnée sauvegardée",
                            Toast.LENGTH_LONG).show();
                }
                cityDAO.close();
            }
        });
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((City) resultValue).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null && constraint.length() > 3) {
                suggestions.clear();

                cityDAO.open();
                tempItems = cityDAO.getCityLike(constraint.toString()).getCities();
                cityDAO.close();

                for (City city : tempItems) {
                    if (city.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(city);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<City> filterList = (ArrayList<City>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (City city : filterList) {
                    add(city);
                    notifyDataSetChanged();
                }
            }
        }
    };

}
