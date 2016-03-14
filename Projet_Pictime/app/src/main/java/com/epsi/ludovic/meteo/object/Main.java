package com.epsi.ludovic.meteo.object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Betty on 06/03/2016.
 */
public class Main implements Serializable {

    @SerializedName("temp")
    public String temp ;


    @SerializedName("pressure")
    public String pressure;

    @SerializedName("humidity")
    public String humidity;

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public Main(String temp, String pressure, String humidity) {
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    @Override
    public String toString() {
        return "Main{" +
                "temp='" + temp + '\'' +
                ", pressure='" + pressure + '\'' +
                ", humidity='" + humidity + '\'' +
                '}';
    }
}
