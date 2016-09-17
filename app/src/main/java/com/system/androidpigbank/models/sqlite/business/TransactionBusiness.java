package com.system.androidpigbank.models.sqlite.business;

import android.content.Context;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.system.androidpigbank.controllers.vos.MonthVO;
import com.system.androidpigbank.controllers.vos.TitleVO;
import com.system.androidpigbank.controllers.vos.TotalVO;
import com.system.androidpigbank.controllers.vos.WhiteSpaceVO;
import com.system.androidpigbank.controllers.vos.CategoryVO;
import com.system.androidpigbank.controllers.vos.TransactionVO;
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
public class TransactionBusiness extends DaoAbs<TransactionVO> {

    public TransactionBusiness(Context context) {
        super(context);
    }

    public TransactionVO save(TransactionVO transaction) throws SQLException {

        if (transaction.getCategory().getId() == null) {
            CategoryVO category = new CategoryBusiness(getContext()).save(transaction.getCategory());
            category.setPrimary(true);
            transaction.setCategory(category);
        }

        if (transaction.getCategorySecondary() != null && transaction.getCategorySecondary().getId() == null) {
            CategoryVO category = new CategoryBusiness(getContext()).save(transaction.getCategorySecondary());
            transaction.setCategorySecondary(category);
        }

        return super.save(transaction);
    }

    public List<TransactionVO> getTransactionByMonth(int month, int year) throws SQLException {

        ConnectionSource connection = new AndroidConnectionSource(db);
        Dao<TransactionVO, String> dao = DaoManager.createDao(connection, TransactionVO.class);
        QueryBuilder<TransactionVO, String> queryTransaction = dao.queryBuilder();

        Date cInit = JavaUtils.DateUtil.getActualMaximum(year, month - 1);
        Date cEnd = JavaUtils.DateUtil.getActualMaximum(year, month);

        queryTransaction.where().between("datePayment", cInit, cEnd);
        queryTransaction.orderBy("datePayment", true);

        Dao<CategoryVO, String> categoryDao = DaoManager.createDao(connection, CategoryVO.class);
        QueryBuilder<CategoryVO, String> queryCategory = categoryDao.queryBuilder();

        List<TransactionVO> results = queryTransaction.join(queryCategory).query();
        connection.close();

        return results;
    }

    public List<TransactionVO> findByCategory(CategoryVO category, int month, int year) throws SQLException {

        Date cInit = JavaUtils.DateUtil.getActualMaximum(year, month - 1);
        Date cEnd = JavaUtils.DateUtil.getActualMaximum(year, month);

        ConnectionSource connection = new AndroidConnectionSource(db);

        Dao<TransactionVO, String> dao = DaoManager.createDao(connection, TransactionVO.class);
        QueryBuilder<TransactionVO, String> queryTransaction = dao.queryBuilder();

        Where<TransactionVO, String> where = queryTransaction.where();

        where.and(
                where.between("datePayment", cInit, cEnd),
                where.or(
                        where.eq("category_id", category.getId()),
                        where.eq("categorySecondary_id", category.getId())
                )
        );

        queryTransaction.orderBy("category_id", false).orderBy("categorySecondary_id", false).orderBy("datePayment", true);

        List<TransactionVO> results = queryTransaction.query();

        connection.close();
        return results;
    }

    public TransactionVO delete(TransactionVO transaction) throws SQLException {
        ConnectionSource connection = new AndroidConnectionSource(db);
        Dao<TransactionVO, String> dao = DaoManager.createDao(connection, TransactionVO.class);
        dao.delete(transaction);
        return transaction;
    }

    public TransactionVO edit(TransactionVO transaction) throws SQLException {
        ConnectionSource connection = new AndroidConnectionSource(db);
        Dao<TransactionVO, String> dao = DaoManager.createDao(connection, TransactionVO.class);
        dao.update(transaction);
        return transaction;
    }

    public List<MonthVO> getMonthWithTransaction(int year) throws SQLException {

        List<MonthVO> list = new ArrayList<>();

        for (int month = 11; month >= 0; month--) {
            List<TransactionVO> transactions = getTransactionByMonth(month, year);

            Double amount = 0D;
            for (TransactionVO transaction : transactions) {
                amount += transaction.getValue();
            }

            if (amount > 0) {
                list.add(new MonthVO(month, year, amount));
            }
        }

        return list;
    }

    public List<CardAdapter.CardModel> organizeTransationcListV2(List<TransactionVO> list) {

        List<CardAdapter.CardModel> itens = new ArrayList<>();

        Double valorAcumular = 0D;
       // Double valorDiario = 0D;
        boolean hasTitleFutureLanc = false;

        itens.add(new WhiteSpaceVO());

        for (int position = 0; position < list.size(); position++){

            TransactionVO transactionAct = list.get(position);
            TransactionVO transactionProx = list.size() > position + 1 ? list.get(position + 1) : null;

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
