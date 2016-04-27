package com.system.androidpigbank.models.persistences;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Created by eferraz on 05/12/15.
 */
public class SQLiteCustomHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "m4.db";
    private static final int DATABASE_VERSION = 1;
    private final Context context;

    public SQLiteCustomHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
    }
}
