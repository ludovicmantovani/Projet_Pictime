package com.epsi.ludovic.meteo.object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Betty on 15/02/2016.
 */
public class City implements Serializable {

    @SerializedName("name")
    private String name;

    private long technical_id;

    @SerializedName("id")
    private String id;

    @SerializedName("coord")
    private Coord coord;

    @SerializedName("main")
    private Main main;

    @SerializedName("weather")
    private List<Weather> weather;

    @SerializedName("wind")
    private Wind wind;

    private Date update = null;

    private int favorite = 0;

    public City(String name) {
        this.name = name;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

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

    public long getTechnical_id() {
        return technical_id;
    }

    public void setTechnical_id(long technical_id) {
        this.technical_id = technical_id;
    }

    public Boolean isFavorite() {
        return (this.favorite == 1);
    }

    public void setFavorite(Boolean etat) {
        if (etat == true)
        {this.favorite = 1;}
        else
        {this.favorite = 0;}
    }

    @Override
    public String toString() {
        return "City{" +
                "name='" + name + '\'' +
                ", technical_id=" + technical_id +
                ", id='" + id + '\'' +
                ", coord=" + coord +
                ", main=" + main +
                ", weather=" + weather +
                ", wind=" + wind +
                ", update=" + update +
                ", favorite=" + favorite +
                '}';
    }
}
