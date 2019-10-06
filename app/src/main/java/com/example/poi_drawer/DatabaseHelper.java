package com.example.poi_drawer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * The DatabaseHelper class is used to set up an SQLite database to save all Points of Interest.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="register.db";
    public static final String TABLE_NAME="points of interest";
    public static final String COL_1="title";
    public static final String COL_2="category";
    public static final String COL_3="image";
    public static final String COL_4="comments";
    public static final String COL_5="latitude";
    public static final String COL_6="longitude";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    /**
     * Creates a table to store Points of Interest.
     * @param db the table to store Points of Interest in.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "(title TEXT PRIMARY KEY NOT NULL, " +
                "category TEXT, " +
                "image TEXT, " +
                "comments TEXT) + " +
                "latitude FLOAT(4,10) + " +
                "longitude FLOAT(4,10)");
    }

    /**
     * re-creates table upon re-opening the application.
     *
     * @param db the database to be dropped and re-created.
     * @param i unused parameter, but necessary to allow method overriding from interface.
     * @param i1 unused parameter, but necessary to allow method overriding from interface.
     */

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); //Drop older table if it exists.
        onCreate(db);
    }
}
