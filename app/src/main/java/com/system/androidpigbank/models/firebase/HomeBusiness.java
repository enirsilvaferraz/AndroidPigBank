package com.system.androidpigbank.models.firebase;

import android.support.annotation.NonNull;

import com.system.androidpigbank.controllers.vos.HomeObjectVO;
import com.system.androidpigbank.controllers.vos.Month;
import com.system.androidpigbank.models.sqlite.entities.Category;
import com.system.androidpigbank.models.sqlite.entities.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Enir on 13/09/2016.
 */

public class HomeBusiness {

    public void findAll(final int month, int year, @NonNull final SingleResult listener) {

        final HomeObjectVO homeVO = new HomeObjectVO();
        homeVO.setMonth(month);
        homeVO.setYear(year);

        getTransactions(month, year, listener, homeVO);

    }

    private void getTransactions(int month, int year, @NonNull final SingleResult listener, final HomeObjectVO homeVO) {

        new TransactionFirebaseBusiness().findTransactionByMonth(month, year, new FirebaseDaoAbs.FirebaseMultiReturnListener<Transaction>() {
            @Override
            public void onFindAll(List<Transaction> list) {

                homeVO.setListTransaction(list);
                organizeHome(homeVO);

                //getCategories(homeVO, listener);

                listener.onFind(homeVO);

            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });
    }

    private void getCategories(final HomeObjectVO homeVO, @NonNull final SingleResult listener) {
        new CategoryFirebaseBusiness().findAll(new FirebaseDaoAbs.FirebaseMultiReturnListener<Category>() {
            @Override
            public void onFindAll(List<Category> list) {

                homeVO.setListCategorySummary(list);

                getMonth(homeVO, listener);
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });
    }

    private void getMonth(final HomeObjectVO homeVO, @NonNull final SingleResult listener) {
        new MonthFirebaseBusiness().findAll(new FirebaseDaoAbs.FirebaseMultiReturnListener<Month>() {
            @Override
            public void onFindAll(List<Month> list) {

                homeVO.setListMonth(list);
                listener.onFind(homeVO);
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });
    }

    private void organizeHome(HomeObjectVO vo) {

        List<Category> innerList = new ArrayList<>();

        for (Transaction transaction : vo.getListTransaction()) {
            getCategoryIndex(innerList, transaction, transaction.getCategory());
            getCategoryIndex(innerList, transaction, transaction.getCategorySecondary());
        }

        vo.setListCategorySummary(innerList);

        Month month = new Month();
        month.setMonth(vo.getMonth());
        month.setYear(vo.getYear());
        month.setValue(0D);

        for (Category category:innerList){
            month.setValue(month.getValue() + category.getAmount());
        }

        List<Month> months = new ArrayList<>();
        months.add(month);

        vo.setListMonth(months);
    }

    private void getCategoryIndex(List<Category> innerList, Transaction transaction, Category categorySecondary) {
        if (categorySecondary != null) {
            if (!innerList.contains(categorySecondary)) {
                categorySecondary.setAmount(transaction.getValue());
                categorySecondary.setTransactionList(new ArrayList<Transaction>());
                categorySecondary.getTransactionList().add(transaction);
                innerList.add(categorySecondary);
            } else {
                Category category = innerList.get(innerList.indexOf(categorySecondary));
                category.setAmount(category.getAmount() + transaction.getValue());
                category.getTransactionList().add(transaction);
            }
        }
    }

    /**
     *
     */
    public interface SingleResult {

        void onFind(HomeObjectVO vo);

        void onError(String error);
    }
}
