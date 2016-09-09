package com.system.androidpigbank.models.sqlite.business;

import android.content.Context;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.system.architecture.helpers.JavaHelper;
import com.system.androidpigbank.models.sqlite.entities.Category;
import com.system.androidpigbank.models.sqlite.entities.Transaction;
import com.system.androidpigbank.models.sqlite.persistences.DaoAbs;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by eferraz on 05/12/15.
 */
public class CategoryBusiness extends DaoAbs<Category> {

    public CategoryBusiness(Context context) {
        super(context);
    }

    public List<Category> getSummaryCategoryByMonth(int month, int year) throws Exception {

        List<Category> categories = findAll();
        for (Category category : categories) {

            category.setTransactionList(new TransactionBusiness(getContext()).findByCategory(category, month, year));

            Double amount = 0d;
            for (Transaction transaction : category.getTransactionList()) {
                amount += transaction.getValue();
            }

            category.setAmount(amount);
        }

        return categories;
    }

    @Override
    public List<Category> findAll() throws SQLException {

        ConnectionSource connectionSource = new AndroidConnectionSource(db);
        Dao<Category, String> dao = DaoManager.createDao(connectionSource, JavaHelper.getTClass(this));

        QueryBuilder<Category, String> queryBuilder = dao.queryBuilder();
        queryBuilder.orderBy("primary", false).orderBy("name", true);

        List<Category> list = queryBuilder.query();
        connectionSource.close();

        return list;
    }

    //    public List<Category> findAll() throws Exception {
//        ConnectionSource connectionSource = new AndroidConnectionSource(db);
//        Dao<Category, String> accountDao = DaoManager.createDao(connectionSource, Category.class);
//
//        List<Category> list = accountDao.queryForAll();
//        connectionSource.close();
//
//        return list;
//    }

//    public Category save(Category category) throws SQLException {
//
//        ConnectionSource connectionSource = new AndroidConnectionSource(db);
//        Dao<Category, String> accountDao = DaoManager.createDao(connectionSource, Category.class);
//
//        accountDao.create(category);
//        connectionSource.close();
//
//        return category;
//    }
}
