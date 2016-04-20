package com.system.androidpigbank.models.business;

import android.content.Context;

import com.firebase.client.Firebase;
import com.google.gson.Gson;
import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.system.androidpigbank.models.entities.Category;
import com.system.androidpigbank.models.entities.EntityAbs;
import com.system.androidpigbank.models.entities.Transaction;
import com.system.androidpigbank.models.persistences.DaoAbs;

import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eferraz on 05/12/15.
 */
public class CategoryBusiness extends DaoAbs<Category> {

    public CategoryBusiness(Context context) {
        super(context);

        try {
            ConnectionSource connectionSource = new AndroidConnectionSource(db);
            TableUtils.createTableIfNotExists(connectionSource, Category.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Category deleteLogic(Category entity) throws SQLException {
        ConnectionSource connection = new AndroidConnectionSource(db);
        Dao<Category, String> dao = DaoManager.createDao(connection, Category.class);
        dao.delete(entity);
        return entity;
    }

    public Category edit(Category entity) throws SQLException {
        ConnectionSource connection = new AndroidConnectionSource(db);
        Dao<Category, String> dao = DaoManager.createDao(connection, Category.class);
        dao.update(entity);
        return entity;
    }

    public List<Category> getChartDataByMonth(int month) throws Exception {

        List<Category> categories = findAll();
        for (Category category : categories) {
            List<Transaction> transactions = new TransactionBusiness(getContext()).findByCategory(category, month);

            Double amount = 0d;
            for (Transaction transaction : transactions) {
                amount += transaction.getValue();
            }
            category.setAmount(amount.floatValue());
        }

        return categories;
    }

    public List<Category> findAll() throws Exception {
        ConnectionSource connectionSource = new AndroidConnectionSource(db);
        Dao<Category, String> dao = DaoManager.createDao(connectionSource, Category.class);

        QueryBuilder<Category, String> queryBuilder = dao.queryBuilder();
        queryBuilder.where().eq("alreadySync", false);

        List<Category> list = queryBuilder.query();
        connectionSource.close();

        return list;
    }

    public Category save(Category category) throws SQLException {

        category.setActive(true);

        ConnectionSource connectionSource = new AndroidConnectionSource(db);
        Dao<Category, String> accountDao = DaoManager.createDao(connectionSource, Category.class);

        accountDao.create(category);
        connectionSource.close();

        return category;
    }
}
