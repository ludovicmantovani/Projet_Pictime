package com.epsi.ludovic.meteo.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ludovic on 05/02/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(Context context, String name, CursorFactory factory, int version) {

        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BddNameConvention.CITY_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(BddNameConvention.CITY_TABLE_DROP);
        onCreate(db);
    }
}
