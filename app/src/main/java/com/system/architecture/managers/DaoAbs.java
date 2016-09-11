package com.system.architecture.managers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.system.androidpigbank.models.sqlite.persistences.SQLiteCustomHelper;
import com.system.architecture.helpers.JavaHelper;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by eferraz on 05/12/15.
 */
public abstract class DaoAbs<T extends EntityAbs> {

    private final Context context;
    protected SQLiteDatabase db;
    private SQLiteCustomHelper sqLiteCustomHelper;

    public DaoAbs(Context context) {
        this.context = context;
        sqLiteCustomHelper = new SQLiteCustomHelper(context);
        db = sqLiteCustomHelper.getWritableDatabase();
    }

    public Context getContext() {
        return context;
    }

    public List<T> findAll() throws SQLException {

        ConnectionSource connection = new AndroidConnectionSource(db);
        Dao<T, String> dao = DaoManager.createDao(connection, JavaHelper.getTClass(this));

        List<T> list = dao.queryForAll();
        connection.close();

        return list;
    }

    public T save(T entity) throws SQLException {

        ConnectionSource connection = new AndroidConnectionSource(db);
        Dao<T, String> dao = DaoManager.createDao(connection, JavaHelper.getTClass(this));

        dao.createOrUpdate(entity);
        connection.close();

        return entity;
    }

    public T delete(T entity) throws SQLException {

        ConnectionSource connection = new AndroidConnectionSource(db);
        Dao<T, String> dao = DaoManager.createDao(connection, JavaHelper.getTClass(this));

        dao.delete(entity);
        connection.close();

        return entity;
    }

    public T edit(T entity) throws SQLException {

        ConnectionSource connection = new AndroidConnectionSource(db);
        Dao<T, String> dao = DaoManager.createDao(connection, JavaHelper.getTClass(this));

        dao.update(entity);
        connection.close();

        return entity;
    }
}
