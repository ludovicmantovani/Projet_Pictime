package com.epsi.ludovic.meteo.object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Betty on 06/03/2016.
 */
public class Wind implements Serializable {

    @SerializedName("speed")
    public String speed;

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public Wind(String speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "Wind{" +
                "speed='" + speed + '\'' +
                '}';
    }
}
