package com.epsi.ludovic.meteo.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Ludovic on 11/03/2016.
 */
public abstract class DAOBase {

    // Batabase's version
    protected final static int VERSION = 1;

    // File's name
    protected final static String NOM = "pictime_project_database.db";

    protected SQLiteDatabase mDb = null;
    protected DatabaseHandler dbHandler = null;

    public DAOBase(Context context){
        this.dbHandler = new DatabaseHandler(context, NOM, null, VERSION);
    }

    public SQLiteDatabase open() {
        mDb = dbHandler.getWritableDatabase();
        return mDb;
    }

    public void close() {
        mDb.close();
    }

    public SQLiteDatabase getDb() {
        return mDb;
    }

    public String getDatabaseName(){ return NOM; }

}
