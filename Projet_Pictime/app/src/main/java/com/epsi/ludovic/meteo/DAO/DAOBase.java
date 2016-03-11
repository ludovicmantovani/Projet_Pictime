package com.epsi.ludovic.meteo.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Ludovic on 11/03/2016.
 */
public abstract class DAOBase {

    // Version de la base
    protected final static int VERSION = 1;

    // Le nom du fichier de la base
    protected final static String NOM = "pictime_project_database.db";

    protected SQLiteDatabase mDb = null;
    protected DatabaseHandler dbHandler = null;

    public DAOBase(Context pContext) {
        this.dbHandler = new DatabaseHandler(pContext, NOM, null, VERSION);
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
