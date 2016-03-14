package com.epsi.ludovic.meteo.DAO;

/**
 * Created by Ludovic on 12/03/2016.
 */
public abstract class BddNameConvention {

    public static final String DATABASE_NAME = "pictime_project_database.db";
    public static final String CITY_KEY = "technical_id";
    public static final String CITY_NAME = "name";
    public static final String CITY_ID = "id";
    public static final String CITY_COORD_LON = "coord_lon";
    public static final String CITY_COORD_LAT = "coord_lat";
    public static final String CITY_DATE = "date";
    public static final String CITY_MAIN_TEMP = "main_temp";
    public static final String CITY_WIND_SPEED = "wind_speed";
    public static final String CITY_WEATHER_MID = "weather_mid";
    public static final String CITY_WEATHER_MMAIN = "weather_mmain";
    public static final String CITY_WEATHER_MDESCRIPTION = "weather_mdescription";
    public static final String CITY_WEATHER_MICON = "weather_micon";
    public static final String CITY_FAVORITE = "favorite";
    public static final String CITY_MAIN_PRESSURE = "main_pressure";
    public static final String CITY_MAIN_HUMIDITY = "main_humidity";


    public static final String CITY_TABLE_NAME = "City";

    public static final String CITY_TABLE_CREATE = "CREATE TABLE " + CITY_TABLE_NAME + " (" +
            CITY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CITY_NAME + " TEXT," +
            CITY_ID + " INTEGER," +
            CITY_COORD_LON + " TEXT," +
            CITY_COORD_LAT + " TEXT," +
            CITY_MAIN_TEMP + " TEXT," +
            CITY_DATE + " TEXT,"+
            CITY_WIND_SPEED + " TEXT," +
            CITY_WEATHER_MID + " INTEGER," +
            CITY_WEATHER_MMAIN + " TEXT," +
            CITY_WEATHER_MDESCRIPTION + " TEXT," +
            CITY_WEATHER_MICON + " TEXT," +
            CITY_FAVORITE + " INTEGER," +
            CITY_MAIN_PRESSURE + " TEXT," +
            CITY_MAIN_HUMIDITY + " TEXT" +
            ");";

    public static final String CITY_TABLE_DROP = "DROP TABLE IF EXISTS " + CITY_TABLE_NAME + ";";
}
