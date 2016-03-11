package com.epsi.ludovic.meteo.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ludovic on 05/02/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String CITY_KEY = "technical_id";
    public static final String CITY_ID = "id";
    public static final String CITY_COORD = "coord";
    public static final String CITY_MAIN = "main";
    public static final String CITY_WEATHER = "weather";
    public static final String CITY_WIND = "wind";
    public static final String CITY_DATE = "date";

    public static final String CITY_TABLE_NAME = "City";


    public static final String CITY_TABLE_CREATE = "CREATE TABLE " + CITY_TABLE_NAME + " (" +
            CITY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CITY_ID + "INTEGER," +
            CITY_COORD + "NONE" +
            CITY_MAIN + "NONE" +
            CITY_WEATHER + "NONE" +
            CITY_WIND + "NONE" +
            CITY_DATE + "NUMERIC"+
            ");";

    public static final String CITY_TABLE_DROP = "DROP TABLE IF EXISTS " +
            CITY_TABLE_NAME + ";";

    public DatabaseHandler(Context context, String name, CursorFactory factory, int version) {

        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CITY_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CITY_TABLE_DROP);
        onCreate(db);
    }
}
