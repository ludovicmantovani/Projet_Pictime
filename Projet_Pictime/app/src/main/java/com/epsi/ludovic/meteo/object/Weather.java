package com.epsi.ludovic.meteo.object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Betty on 06/03/2016.
 */
public class Weather implements Serializable {


    @SerializedName("id")
    private int mId;

    @SerializedName("main")
    private String mMain;

    @SerializedName("description")
    private String mDescription;

    @SerializedName("icon")
    private String mIcon;

    public Weather(int id, String main, String description, String icon) {
        mId = id;
        mMain = main;
        mDescription = description;
        mIcon = icon;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getMain() {
        return mMain;
    }

    public void setMain(String main) {
        mMain = main;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    @Override
    public String toString() {
        return "id: " + mId + "\n" +
                "main: " + mMain + "\n" +
                "description: " + mDescription + "\n" +
                "icon: " + mIcon;
    }
}
