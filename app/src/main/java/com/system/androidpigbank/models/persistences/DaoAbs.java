package com.system.androidpigbank.models.persistences;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.system.androidpigbank.models.entities.EntityAbs;

import java.lang.reflect.ParameterizedType;
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

    public List<T> findAll() throws SQLException {

        final ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Class<T> theType = (Class<T>) (type).getActualTypeArguments()[0];

        ConnectionSource connectionSource = new AndroidConnectionSource(db);
        Dao<T, String> accountDao = DaoManager.createDao(connectionSource, theType);

        List<T> list = accountDao.queryForAll();
        connectionSource.close();

        return list;
    }
}
