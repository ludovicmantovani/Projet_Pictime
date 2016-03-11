package com.epsi.ludovic.meteo.object;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Betty on 06/03/2016.
 */
public class Main {

    @SerializedName("temp")
    public String temp ;

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public Main(String temp, String temp_min, String temp_max) {
        this.temp = temp;
    }

    @Override
    public String toString() {
        return "Main{" +
                "temp='" + temp + '\'' +
                '}';
    }
}
