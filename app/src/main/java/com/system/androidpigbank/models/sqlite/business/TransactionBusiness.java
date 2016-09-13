package com.system.androidpigbank.models.sqlite.business;

import android.content.Context;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.system.androidpigbank.controllers.vos.Month;
import com.system.androidpigbank.controllers.vos.TitleVO;
import com.system.androidpigbank.controllers.vos.TotalVO;
import com.system.androidpigbank.controllers.vos.WhiteSpaceVO;
import com.system.androidpigbank.models.sqlite.entities.Category;
import com.system.androidpigbank.models.sqlite.entities.Transaction;
import com.system.architecture.managers.DaoAbs;
import com.system.architecture.adapters.CardAdapter;
import com.system.architecture.utils.JavaUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by eferraz on 05/12/15.
 */
public class TransactionBusiness extends DaoAbs<Transaction> {

    public TransactionBusiness(Context context) {
        super(context);
    }

    public Transaction save(Transaction transaction) throws SQLException {

        if (transaction.getCategory().getId() == null) {
            Category category = new CategoryBusiness(getContext()).save(transaction.getCategory());
            category.setPrimary(true);
            transaction.setCategory(category);
        }

        if (transaction.getCategorySecondary() != null && transaction.getCategorySecondary().getId() == null) {
            Category category = new CategoryBusiness(getContext()).save(transaction.getCategorySecondary());
            transaction.setCategorySecondary(category);
        }

        return super.save(transaction);
    }

    public List<Transaction> getTransactionByMonth(int month, int year) throws SQLException {

        ConnectionSource connection = new AndroidConnectionSource(db);
        Dao<Transaction, String> dao = DaoManager.createDao(connection, Transaction.class);
        QueryBuilder<Transaction, String> queryTransaction = dao.queryBuilder();

        Date cInit = JavaUtils.DateUtil.getActualMaximum(year, month - 1);
        Date cEnd = JavaUtils.DateUtil.getActualMaximum(year, month);

        queryTransaction.where().between("datePayment", cInit, cEnd);
        queryTransaction.orderBy("datePayment", true);

        Dao<Category, String> categoryDao = DaoManager.createDao(connection, Category.class);
        QueryBuilder<Category, String> queryCategory = categoryDao.queryBuilder();

        List<Transaction> results = queryTransaction.join(queryCategory).query();
        connection.close();

        return results;
    }

    public List<Transaction> findByCategory(Category category, int month, int year) throws SQLException {

        Date cInit = JavaUtils.DateUtil.getActualMaximum(year, month - 1);
        Date cEnd = JavaUtils.DateUtil.getActualMaximum(year, month);

        ConnectionSource connection = new AndroidConnectionSource(db);

        Dao<Transaction, String> dao = DaoManager.createDao(connection, Transaction.class);
        QueryBuilder<Transaction, String> queryTransaction = dao.queryBuilder();

        Where<Transaction, String> where = queryTransaction.where();

        where.and(
                where.between("datePayment", cInit, cEnd),
                where.or(
                        where.eq("category_id", category.getId()),
                        where.eq("categorySecondary_id", category.getId())
                )
        );

        queryTransaction.orderBy("category_id", false).orderBy("categorySecondary_id", false).orderBy("datePayment", true);

        List<Transaction> results = queryTransaction.query();

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

    public List<Month> getMonthWithTransaction(int year) throws SQLException {

        List<Month> list = new ArrayList<>();

        for (int month = 11; month >= 0; month--) {
            List<Transaction> transactions = getTransactionByMonth(month, year);

            Double amount = 0D;
            for (Transaction transaction : transactions) {
                amount += transaction.getValue();
            }

            if (amount > 0) {
                list.add(new Month(month, year, amount));
            }
        }

        return list;
    }

    public List<CardAdapter.CardModel> organizeTransationcListV2(List<Transaction> list) {

        List<CardAdapter.CardModel> itens = new ArrayList<>();

        Double valorAcumular = 0D;
       // Double valorDiario = 0D;
        boolean hasTitleFutureLanc = false;

        itens.add(new WhiteSpaceVO());

        for (int position = 0; position < list.size(); position++){

            Transaction transactionAct = list.get(position);
            Transaction transactionProx = list.size() > position + 1 ? list.get(position + 1) : null;

            if (JavaUtils.DateUtil.compare(transactionAct.getDatePayment(), Calendar.getInstance().getTime()) > 0){

                if (!hasTitleFutureLanc && transactionProx != null){
                    itens.add(new TitleVO("Lançamentos Futuros"));
                    itens.add(new WhiteSpaceVO());
                    hasTitleFutureLanc = true;
                }
                valorAcumular += transactionAct.getValue();
            }

            itens.add(transactionAct);
            //valorDiario += transactionAct.getValue();

            if (transactionProx == null || JavaUtils.DateUtil.compare(transactionAct.getDatePayment(), transactionProx.getDatePayment()) != 0) {
                itens.add(new TotalVO(null, valorAcumular));
                itens.add(new WhiteSpaceVO());
                //valorDiario = 0D;
            }
        }

        return itens;
    }
}
