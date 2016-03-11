package com.epsi.ludovic.meteo.object;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by Betty on 15/02/2016.
 */
public class City {

    public String name;

    @SerializedName("id")
    public String id;

    @SerializedName("coord")
    public Coord coord;

    @SerializedName("main")
    public Main main;

    @SerializedName("weather")
    public List<Weather> weather;

    @SerializedName("wind")
    public Wind wind;

    public City(String name) {
        this.name = name;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public Date update;

    public City() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Date getUpdate() {
        return update;
    }

    public void setUpdate(Date update) {
        this.update = update;
    }


    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public Coord getCoord() {

        return coord;
    }

    public void setCoord(Coord coord) {

        this.coord = coord;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    @Override
    public String toString() {
        return "City{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", weather=" + weather +
                ", main=" + main +
                ", coord=" + coord +
                ", wind=" + wind +
                '}';
    }
}
