package com.system.androidpigbank.models.sqlite.business;

import android.content.Context;
import android.support.annotation.NonNull;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.system.androidpigbank.controllers.vos.TitleVO;
import com.system.androidpigbank.controllers.vos.TotalVO;
import com.system.androidpigbank.controllers.vos.TransactionVO;
import com.system.androidpigbank.controllers.vos.WhiteSpaceVO;
import com.system.architecture.adapters.CardAdapter;
import com.system.architecture.helpers.JavaHelper;
import com.system.androidpigbank.controllers.vos.CategoryVO;
import com.system.architecture.managers.DaoAbs;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eferraz on 05/12/15.
 */
public class CategoryBusiness extends DaoAbs<CategoryVO> {

    public CategoryBusiness(Context context) {
        super(context);
    }

    public List<CategoryVO> getSummaryCategoryByMonth(int month, int year) throws Exception {

        List<CategoryVO> categories = findAll();
        for (CategoryVO category : categories) {

            category.setTransactionList(new TransactionBusiness(getContext()).findByCategory(category, month, year));

            Double amount = 0d;
            for (TransactionVO transaction : category.getTransactionList()) {
                amount += transaction.getValue();
            }

            category.setAmount(amount);
        }

        return categories;
    }

    @Override
    public List<CategoryVO> findAll() throws SQLException {

        ConnectionSource connectionSource = new AndroidConnectionSource(db);
        Dao<CategoryVO, String> dao = DaoManager.createDao(connectionSource, JavaHelper.getTClass(this));

        QueryBuilder<CategoryVO, String> queryBuilder = dao.queryBuilder();
        queryBuilder.orderBy("primary", false).orderBy("name", true);

        List<CategoryVO> list = queryBuilder.query();
        connectionSource.close();

        return list;
    }

    public List<CategoryVO> findAllPrimaries() throws SQLException {

        ConnectionSource connectionSource = new AndroidConnectionSource(db);
        Dao<CategoryVO, String> dao = DaoManager.createDao(connectionSource, JavaHelper.getTClass(this));

        QueryBuilder<CategoryVO, String> queryBuilder = dao.queryBuilder();
        queryBuilder.where().eq("primary", true);
        queryBuilder.orderBy("primary", false).orderBy("name", true);

        List<CategoryVO> list = queryBuilder.query();
        connectionSource.close();

        return list;
    }

    @NonNull
    public List<CardAdapter.CardModel> organizeCategorySummaryList(List<CategoryVO> data) {

        boolean hasTitleSecondary = false;
        List<CardAdapter.CardModel> itens = new ArrayList<>();

        for (int position = 0; position < data.size(); position++) {

            CategoryVO category = data.get(position);

            itens.add(new WhiteSpaceVO());
            itens.add(category);

            if (!category.getTransactionList().isEmpty()) {
                itens.addAll(getTransactionByCategory(category.getTransactionList()));
            }

            if (!hasTitleSecondary && !category.isPrimary() && position != data.size() - 1) {
                itens.add(new WhiteSpaceVO());
                itens.add(new TitleVO("Secondary Categories"));
                hasTitleSecondary = true;
            }
        }

        itens.add(new WhiteSpaceVO());

        return itens;
    }

    private List<CardAdapter.CardModel> getTransactionByCategory(List<TransactionVO> list) {

        Double value = 0D;

        List<CardAdapter.CardModel> itens = new ArrayList<>();
        for (int position = 0; position < list.size(); position++) {

            TransactionVO transactionAct = list.get(position);
            TransactionVO transactionProx = list.size() > position + 1 ? list.get(position + 1) : null;

            Long categoryAct = transactionAct.getCategorySecondary() != null ? transactionAct.getCategorySecondary().getId() : -1;
            Long categoryProx = transactionProx != null && transactionProx.getCategorySecondary() != null ? transactionProx.getCategorySecondary().getId() : -1;

            itens.add(transactionAct);
            value += transactionAct.getValue();

            if (transactionProx == null || !categoryAct.equals(categoryProx)) {
                itens.add(new TotalVO(null, value));
                value = 0D;
            }
        }

        return itens;
    }
}
