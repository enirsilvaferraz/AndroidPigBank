package com.system.androidpigbank.models.business;

import android.content.Context;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.system.androidpigbank.controllers.vos.Month;
import com.system.androidpigbank.helpers.JavaHelper;
import com.system.androidpigbank.models.entities.Category;
import com.system.androidpigbank.models.entities.Transaction;
import com.system.androidpigbank.models.persistences.DaoAbs;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by eferraz on 05/12/15.
 */
public class TransactionBusiness extends DaoAbs<Transaction> {

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

    public Transaction save(Transaction transaction) throws SQLException {

        if (transaction.getCategory().getId() == null) {
            Category category = new CategoryBusiness(getContext()).save(transaction.getCategory());
            transaction.setCategory(category);
        }

        return super.save(transaction);
    }

    public List<Transaction> getTransactionByMonth(int month, int year) throws SQLException {

        ConnectionSource connection = new AndroidConnectionSource(db);
        Dao<Transaction, String> dao = DaoManager.createDao(connection, Transaction.class);
        QueryBuilder<Transaction, String> queryTransaction = dao.queryBuilder();

        Calendar cInit = Calendar.getInstance();
        cInit.set(Calendar.YEAR, year);
        cInit.set(Calendar.MONTH, month);
        cInit.set(Calendar.DATE, cInit.getActualMinimum(Calendar.DATE));
        cInit.set(Calendar.HOUR, cInit.getActualMinimum(Calendar.HOUR));
        cInit.set(Calendar.MINUTE, cInit.getActualMinimum(Calendar.MINUTE));
        cInit.set(Calendar.SECOND, cInit.getActualMinimum(Calendar.SECOND));

        Calendar cEnd = Calendar.getInstance();
        cEnd.set(Calendar.YEAR, year);
        cEnd.set(Calendar.MONTH, month);
        cEnd.set(Calendar.DATE, cEnd.getActualMaximum(Calendar.DATE));
        cEnd.set(Calendar.HOUR, cEnd.getActualMaximum(Calendar.HOUR));
        cEnd.set(Calendar.MINUTE, cEnd.getActualMaximum(Calendar.MINUTE));
        cEnd.set(Calendar.SECOND, cEnd.getActualMaximum(Calendar.SECOND));

        queryTransaction.where().between("date", cInit.getTime(), cEnd.getTime());
        queryTransaction.orderBy("date", true);

        Dao<Category, String> categoryDao = DaoManager.createDao(connection, Category.class);
        QueryBuilder<Category, String> queryCategory = categoryDao.queryBuilder();

        List<Transaction> results = queryTransaction.join(queryCategory).query();
        connection.close();

        return results;
    }

    public List<Transaction> findByCategory(Category category, int month, int year) throws SQLException {

        ConnectionSource connection = new AndroidConnectionSource(db);
        Dao<Transaction, String> dao = DaoManager.createDao(connection, Transaction.class);
        QueryBuilder<Transaction, String> queryTransaction = dao.queryBuilder();

        Calendar cInit = Calendar.getInstance();
        cInit.set(Calendar.YEAR, year);
        cInit.set(Calendar.MONTH, month);
        cInit.set(Calendar.DATE, cInit.getActualMinimum(Calendar.DATE));
        cInit.set(Calendar.HOUR, cInit.getActualMinimum(Calendar.HOUR));
        cInit.set(Calendar.MINUTE, cInit.getActualMinimum(Calendar.MINUTE));
        cInit.set(Calendar.SECOND, cInit.getActualMinimum(Calendar.SECOND));

        Calendar cEnd = Calendar.getInstance();
        cEnd.set(Calendar.YEAR, year);
        cEnd.set(Calendar.MONTH, month);
        cEnd.set(Calendar.DATE, cEnd.getActualMaximum(Calendar.DATE));
        cEnd.set(Calendar.HOUR, cEnd.getActualMaximum(Calendar.HOUR));
        cEnd.set(Calendar.MINUTE, cEnd.getActualMaximum(Calendar.MINUTE));
        cEnd.set(Calendar.SECOND, cEnd.getActualMaximum(Calendar.SECOND));

        queryTransaction.where().between("date", cInit.getTime(), cEnd.getTime());

        Dao<Category, String> categoryDao = DaoManager.createDao(connection, Category.class);
        QueryBuilder<Category, String> queryCategory = categoryDao.queryBuilder();
        queryCategory.where().eq("id", category.getId());

        List<Transaction> results = queryTransaction.join(queryCategory).query();
        connection.close();

        return results;
    }

    public Transaction delete(Transaction transaction) throws SQLException {
        ConnectionSource connection = new AndroidConnectionSource(db);
        Dao<Transaction, String> dao = DaoManager.createDao(connection, Transaction.class);
        dao.delete(transaction);
        return transaction;
    }

    public Transaction edit(Transaction transaction) throws SQLException {
        ConnectionSource connection = new AndroidConnectionSource(db);
        Dao<Transaction, String> dao = DaoManager.createDao(connection, Transaction.class);
        dao.update(transaction);
        return transaction;
    }

    public List<Month> getMonthWithTransaction(int year) throws SQLException  {

        List<Month> list = new ArrayList<>();

        for(int month = 0; month < 12; month++) {
            List<Transaction> transactions = getTransactionByMonth(month, year);

            Double amount = 0D;
            for (Transaction transaction : transactions) {
                amount += transaction.getValue();
            }

            if (amount > 0){
                list.add(new Month(month, year, amount));
            }
        }

        return list;
    }
}
