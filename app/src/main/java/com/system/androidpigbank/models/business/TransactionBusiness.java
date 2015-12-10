package com.system.androidpigbank.models.business;

import android.content.Context;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.system.androidpigbank.models.entities.Category;
import com.system.androidpigbank.models.entities.Transaction;
import com.system.androidpigbank.models.persistences.DaoAbs;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by eferraz on 05/12/15.
 */
public class TransactionBusiness extends DaoAbs {

    public TransactionBusiness(Context context) {
        super(context);

        try {
            ConnectionSource connectionSource = new AndroidConnectionSource(db);
            TableUtils.createTableIfNotExists(connectionSource, Category.class);
            TableUtils.createTableIfNotExists(connectionSource, Transaction.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Transaction save(Transaction transaction) throws Exception {

        if (transaction.getCategory().getId() == null) {
            Category category = new CategoryBusiness(getContext()).save(transaction.getCategory());
            transaction.setCategory(category);
        }

        ConnectionSource connectionSource = new AndroidConnectionSource(db);
        Dao<Transaction, String> accountDao = DaoManager.createDao(connectionSource, Transaction.class);

        accountDao.create(transaction);
        connectionSource.close();

        return transaction;
    }

    public List<Transaction> getCurrentMonth() throws Exception {

        ConnectionSource connectionSource = new AndroidConnectionSource(db);
        Dao<Transaction, String> accountDao = DaoManager.createDao(connectionSource, Transaction.class);
        QueryBuilder<Transaction, String> aqb = accountDao.queryBuilder();

        Dao<Category, String> accountDao2 = DaoManager.createDao(connectionSource, Category.class);
        QueryBuilder<Category, String> bqb = accountDao2.queryBuilder();

        List<Transaction> results = aqb.join(bqb).query();
        connectionSource.close();

        return results;
    }

    public List<Transaction> findByCategory(Category category) throws SQLException {

        ConnectionSource connectionSource = new AndroidConnectionSource(db);
        Dao<Transaction, String> accountDao = DaoManager.createDao(connectionSource, Transaction.class);
        QueryBuilder<Transaction, String> aqb = accountDao.queryBuilder();

        Dao<Category, String> accountDao2 = DaoManager.createDao(connectionSource, Category.class);
        QueryBuilder<Category, String> bqb = accountDao2.queryBuilder();
        bqb.where().eq("id", category.getId());

        List<Transaction> results = aqb.join(bqb).query();
        connectionSource.close();

        return results;
    }
}
