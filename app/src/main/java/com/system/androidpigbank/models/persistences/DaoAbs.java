package com.system.androidpigbank.models.persistences;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.system.androidpigbank.models.entities.EntityAbs;
import com.system.androidpigbank.models.entities.Transaction;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by eferraz on 05/12/15.
 */
public abstract class DaoAbs<T extends EntityAbs> {

    private final Context context;
    protected SQLiteDatabase db;
    protected SQLiteCustomHelper sqLiteCustomHelper;

    public DaoAbs(Context context) {
        this.context = context;
        sqLiteCustomHelper = new SQLiteCustomHelper(context);
        db = sqLiteCustomHelper.getWritableDatabase();
    }

    public Context getContext() {
        return context;
    }

    public abstract T edit(T entity) throws SQLException;
    public abstract T deleteLogic(T entity) throws SQLException;
    public abstract List<T> findAll() throws Exception;
    public abstract T save(T entity) throws Exception;
}
