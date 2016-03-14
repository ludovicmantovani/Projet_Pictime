package com.epsi.ludovic.meteo.object;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ludovic on 04/02/2016.
 */
public class Cities {
    @SerializedName("cities")
    private List<City> cities;

    public Cities(List<City> cities) {
        this.cities = cities;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public int size(){
        return cities.size();
    }

    public City get(int index){
        return cities.get(index);
    }

}
