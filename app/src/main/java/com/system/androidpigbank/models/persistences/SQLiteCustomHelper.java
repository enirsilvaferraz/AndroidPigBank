package com.system.androidpigbank.models.persistences;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by eferraz on 05/12/15.
 */
public class SQLiteCustomHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "wonders.db";
    private static final int DATABASE_VERSION = 1;

    public SQLiteCustomHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
