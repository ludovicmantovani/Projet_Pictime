package com.epsi.ludovic.meteo.object;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Betty on 06/03/2016.
 */
public class Coord {

    @SerializedName("lon")
    private String lon;
    @SerializedName("lat")
    private String lat;

    /**
     *
     * @return
     * The lon
     */
    public String getLon() {
        return lon;
    }

    /**
     *
     * @param lon
     * The lon
     */
    public void setLon(String lon) {
        this.lon = lon;
    }

    /**
     *
     * @return
     * The lat
     */
    public String  getLat() {
        return lat;
    }

    /**
     *
     * @param lat
     * The lat
     */
    public void setLat(String lat) {
        this.lat = lat;
    }
}
