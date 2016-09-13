package com.system.androidpigbank.models.sqlite.business;

import android.content.Context;
import android.support.annotation.NonNull;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.system.androidpigbank.controllers.vos.TotalVO;
import com.system.androidpigbank.controllers.vos.WhiteSpaceVO;
import com.system.architecture.adapters.CardAdapter;
import com.system.architecture.helpers.JavaHelper;
import com.system.androidpigbank.models.sqlite.entities.Category;
import com.system.androidpigbank.models.sqlite.entities.Transaction;
import com.system.architecture.managers.DaoAbs;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eferraz on 05/12/15.
 */
public class CategoryBusiness extends DaoAbs<Category> {

    public CategoryBusiness(Context context) {
        super(context);
    }

    public List<Category> getSummaryCategoryByMonth(int month, int year) throws Exception {

        List<Category> categories = findAllPrimaries();
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

    public List<Category> findAllPrimaries() throws SQLException {

        ConnectionSource connectionSource = new AndroidConnectionSource(db);
        Dao<Category, String> dao = DaoManager.createDao(connectionSource, JavaHelper.getTClass(this));

        QueryBuilder<Category, String> queryBuilder = dao.queryBuilder();
        queryBuilder.where().eq("primary", true);
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

    @NonNull
    public List<CardAdapter.CardModel> organizeCategorySummaryList( List<Category> data ) {

        List<CardAdapter.CardModel> itens = new ArrayList<>();

        for (Category category : data) {

            itens.add(new WhiteSpaceVO());
            itens.add(category);

            if (!category.getTransactionList().isEmpty()) {
                itens.addAll(getTransactionByCategory(category.getTransactionList()));
            }
        }

        itens.add(new WhiteSpaceVO());

        return itens;
    }

    private List<CardAdapter.CardModel> getTransactionByCategory(List<Transaction> transactionList) {

        Long categoryAnterior = null;
        Double value = 0D;

        List<CardAdapter.CardModel> innerItens = new ArrayList<>();
        for (int i = 0; i < transactionList.size(); i++) {

            Transaction transaction = transactionList.get(i);
            innerItens.add(transaction);

            value += transaction.getValue();

            Long categoryAct = transaction.getCategorySecondary() != null ? transaction.getCategorySecondary().getId() : -1;

            if (!categoryAct.equals(categoryAnterior) || i == transactionList.size() -1){
                innerItens.add(new TotalVO(null, value));
                value = 0D;
            }

            categoryAnterior = categoryAct;
        }

        return innerItens;
    }
}
