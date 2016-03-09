package com.epsi.ludovic.meteo.object;

import java.util.ArrayList;

/**
 * Created by Ludovic on 24/01/2016.
 */
public class City {

    private int id;
    private String name;
    private ArrayList weather; // Description
    private Object main;
    private String txHumidity;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList getWeather() {
        return weather;
    }

    public void setWeather(ArrayList weather) {
        this.weather = weather;
    }

    public Object getMain() {
        return main;
    }

    public void setMain(Object main) {
        this.main = main;
    }

    public String getTxHumidity() {
        return txHumidity;
    }

    public void setTxHumidity(String txHumidity) {
        this.txHumidity = txHumidity;
    }


    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", weather=" + weather +
                ", main=" + main +
                ", txHumidity='" + txHumidity + '\'' +
                '}';
    }
}
