package com.epsi.ludovic.meteo.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.epsi.ludovic.meteo.object.City;
import com.epsi.ludovic.meteo.object.Coord;
import com.epsi.ludovic.meteo.object.DataSearch;
import com.epsi.ludovic.meteo.object.Main;
import com.epsi.ludovic.meteo.object.Weather;
import com.epsi.ludovic.meteo.object.Wind;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ludovic on 9/03/2016.
 */
public class CityDAO extends DAOBase {


    public CityDAO(Context context){
        super(context);
    }

    //Convert row to city object
    private City cursorToCity(Cursor cursor)
    {
        City newCity = new City();
        Coord newCoord = new Coord();
        Main newMain = new Main(cursor.getString(5),cursor.getString(13), cursor.getString(14));
        Wind newWind = new Wind(cursor.getString(7));
        Weather newWeather = new Weather(cursor.getInt(8),cursor.getString(9), cursor.getString(10),
                cursor.getString(11));
        ArrayList<Weather> newWeatherArrayList = new ArrayList<Weather>();
        newWeatherArrayList.add(newWeather);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newdate = null;
        try {
            newdate = dateFormat.parse(cursor.getString(6));
        }
        catch (Exception e)
        {
        }

        newCity.setTechnical_id(cursor.getLong(0));
        newCity.setName(cursor.getString(1));
        newCity.setId(cursor.getString(2));
        newCoord.setLon(cursor.getString(3));
        newCoord.setLat(cursor.getString(4));
        newCity.setCoord(newCoord);
        newCity.setMain(newMain);
        newCity.setUpdate(newdate);
        newCity.setWind(newWind);
        newCity.setWeather(newWeatherArrayList);
        newCity.setFavorite((cursor.getInt(12) == 1));

        return (newCity);
    }

    //Add city to BDD
    public void add(City city)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BddNameConvention.CITY_NAME, city.getName());
        contentValues.put(BddNameConvention.CITY_ID, city.getId());
        mDb.insert(BddNameConvention.CITY_TABLE_NAME, null, contentValues);
    }

    //Update a city in BDD
    public int update(City city)
    {
        //Set the format to sql date time*
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newdate = new Date();
        int favoriteValue = 0;

        ContentValues contentValues = new ContentValues();
        contentValues.put(BddNameConvention.CITY_COORD_LON, city.getCoord().getLon());
        contentValues.put(BddNameConvention.CITY_COORD_LAT, city.getCoord().getLat());
        contentValues.put(BddNameConvention.CITY_MAIN_TEMP, city.getMain().getTemp());
        contentValues.put(BddNameConvention.CITY_WIND_SPEED, city.getWind().getSpeed());
        contentValues.put(BddNameConvention.CITY_WEATHER_MID, city.getWeather().get(0).getId());
        contentValues.put(BddNameConvention.CITY_WEATHER_MMAIN, city.getWeather().get(0).getMain());
        contentValues.put(BddNameConvention.CITY_WEATHER_MDESCRIPTION,
                city.getWeather().get(0).getDescription());
        contentValues.put(BddNameConvention.CITY_WEATHER_MICON, city.getWeather().get(0).getIcon());
        contentValues.put(BddNameConvention.CITY_DATE, dateFormat.format(newdate));
        contentValues.put(BddNameConvention.CITY_MAIN_PRESSURE, city.getMain().getPressure());
        contentValues.put(BddNameConvention.CITY_MAIN_HUMIDITY, city.getMain().getHumidity());

        int ret = mDb.update(BddNameConvention.CITY_TABLE_NAME,
                contentValues,
                BddNameConvention.CITY_ID + " = ?",
                new String[]{String.valueOf(city.getId())});

        return(ret);
    }

    //Return favorite cities
    public DataSearch getFavorite()
    {
        ArrayList<City> cityArrayList = new ArrayList<City>();

        Cursor cursor = mDb.rawQuery("select * from " + BddNameConvention.CITY_TABLE_NAME +
                        " where " + BddNameConvention.CITY_FAVORITE + " = ?",
                new String[]{String.valueOf(1)});

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            City city = cursorToCity(cursor);
            cityArrayList.add(city);
            cursor.moveToNext();
        }
        cursor.close();

        return(new DataSearch(cityArrayList));
    }

    //Return cities with a specific name
    public DataSearch getCityLike(String pattern)
    {
        ArrayList<City> cityArrayList = new ArrayList<City>();

        String query = "select * from " + BddNameConvention.CITY_TABLE_NAME +
                " where " + BddNameConvention.CITY_NAME + " like '" + pattern + "%'"
                +" ORDER BY "+ BddNameConvention.CITY_NAME + " ASC";
        Log.d("GETCITYLIKE", query);
        Cursor cursor = mDb.rawQuery(query,null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            City city = cursorToCity(cursor);
            cityArrayList.add(city);
            cursor.moveToNext();
        }
        cursor.close();
        return(new DataSearch(cityArrayList));
    }

    public int setToFavorite(City city, Boolean becomeFavorite) {
        ContentValues contentValues = new ContentValues();
        int favoriteValue = 0;
        if (becomeFavorite){favoriteValue = 1;}
        contentValues.put(BddNameConvention.CITY_FAVORITE, favoriteValue);
        int ret = mDb.update(BddNameConvention.CITY_TABLE_NAME,
                contentValues,
                BddNameConvention.CITY_ID + " = ?",
                new String[]{String.valueOf(city.getId())});
        return ret;
    }

    public City getCityById(String cityId)
    {
        City newCity;
        String query = "select * from " + BddNameConvention.CITY_TABLE_NAME +
                " where " + BddNameConvention.CITY_ID + " = ? ";

        Cursor cursor = mDb.rawQuery(query, new String[]{cityId});

        cursor.moveToFirst();
        newCity = cursorToCity(cursor);
        cursor.close();
        return(newCity);
    }

    public DataSearch getCities()
    {
        ArrayList<City> cityArrayList = new ArrayList<City>();

        Cursor cursor = null;

        cursor = mDb.query(BddNameConvention.CITY_TABLE_NAME, null, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            City city = cursorToCity(cursor);
            cityArrayList.add(city);
            cursor.moveToNext();
        }
        cursor.close();

        return(new DataSearch(cityArrayList));
    }

}
