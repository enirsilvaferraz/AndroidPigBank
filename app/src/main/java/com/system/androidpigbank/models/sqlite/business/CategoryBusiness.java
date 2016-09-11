package com.system.androidpigbank.models.sqlite.business;

import android.content.Context;
import android.support.annotation.NonNull;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.system.androidpigbank.controllers.vos.TotalVO;
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

//        boolean alreadyAddedSecoundary = false;
        //this.itens.add(new TitleVO("Primary Categories"));

        for (Category category : data) {

//            if (!alreadyAddedSecoundary && !category.isPrimary()) {
//                TitleVO titleVO = new TitleVO("Secondary Categories");
//                titleVO.setCardStrategy(CardAdapter.CardModeItem.NO_STRATEGY);
//                itens.add(titleVO);
//                alreadyAddedSecoundary = true;
//            }

            if (category.getTransactionList().isEmpty()) {
                category.setCardStrategy(CardAdapter.CardModeItem.SINGLE);
                itens.add(category);
            } else {
                category.setCardStrategy(CardAdapter.CardModeItem.START);
                itens.add(category);
                itens.addAll(getTransactionByCategory(category));
            }
        }
        return itens;
    }

    private List<CardAdapter.CardModel> getTransactionByCategory(Category category) {

        String nomeCat = null;
        Double value = 0D;

        List<CardAdapter.CardModel> innerItens = new ArrayList<>();
        for (int i = 0; i < category.getTransactionList().size(); i++) {

            Transaction transaction = category.getTransactionList().get(i);
            String name = transaction.getCategorySecondary() != null ? transaction.getCategorySecondary().getName() : "";

            if (!innerItens.isEmpty() && !name.equals(nomeCat)) {
                TotalVO totalVO = new TotalVO(value);
                totalVO.setCardStrategy(CardAdapter.CardModeItem.MIDDLE);
                innerItens.add(totalVO);
                value = 0D;
            }

            nomeCat = name;

            transaction.setCardStrategy(CardAdapter.CardModeItem.MIDDLE);
            innerItens.add(transaction);

            value += transaction.getValue();
            if (i == category.getTransactionList().size() - 1) {
                TotalVO totalVO = new TotalVO(value);
                totalVO.setCardStrategy(CardAdapter.CardModeItem.END);
                innerItens.add(totalVO);
            }
        }

        return innerItens;
    }
}
