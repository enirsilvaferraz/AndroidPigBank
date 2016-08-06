package com.system.androidpigbank.models.business;

import android.content.Context;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.system.androidpigbank.controllers.vos.Month;
import com.system.androidpigbank.models.entities.Category;
import com.system.androidpigbank.models.entities.FixedTransaction;
import com.system.androidpigbank.models.entities.Transaction;
import com.system.androidpigbank.models.persistences.DaoAbs;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by eferraz on 05/12/15.
 */
public class FixedTransactionBusiness extends DaoAbs<FixedTransaction> {

    public FixedTransactionBusiness(Context context) {
        super(context);
    }

    public FixedTransaction save(FixedTransaction transaction) throws SQLException {

        if (transaction.getCategory().getId() == null) {
            Category category = new CategoryBusiness(getContext()).save(transaction.getCategory());
            transaction.setCategory(category);
        }

        if (transaction.getCategorySecondary() != null && transaction.getCategorySecondary().getId() == null) {
            Category category = new CategoryBusiness(getContext()).save(transaction.getCategorySecondary());
            transaction.setCategory(category);
        }

        return super.save(transaction);
    }
}
