package com.epsi.ludovic.meteo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.epsi.ludovic.meteo.DAO.BddNameConvention;
import com.epsi.ludovic.meteo.DAO.CityDAO;
import com.epsi.ludovic.meteo.R;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(getContext(), R.layout.activity_list_item, null);
        City city = getItem(position);
        TextView cityNameTextView = (TextView) view.findViewById(R.id.cityName);
        if (cityNameTextView != null && cityNameTextView instanceof TextView)
        {
            cityNameTextView.setText(city.getName());
        }
        return view;
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
