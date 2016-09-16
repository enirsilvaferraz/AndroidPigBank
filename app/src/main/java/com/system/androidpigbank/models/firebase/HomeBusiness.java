package com.system.androidpigbank.models.firebase;

import android.support.annotation.NonNull;

import com.system.androidpigbank.controllers.vos.CategoryVO;
import com.system.androidpigbank.controllers.vos.HomeObjectVO;
import com.system.androidpigbank.controllers.vos.MonthVO;
import com.system.androidpigbank.controllers.vos.TransactionVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Enir on 13/09/2016.
 */

public class HomeBusiness {

    public void findAll(final int month, final int year, @NonNull final SingleResult listener) {

        final List<TransactionVO> transactions = new ArrayList<>();
        final List<CategoryVO> categories = new ArrayList<>();

        new TransactionFirebaseBusiness().findTransactionByMonth(month, year, new FirebaseDaoAbs.FirebaseMultiReturnListener<TransactionVO>() {
            @Override
            public void onFindAll(List<TransactionVO> list) {
                transactions.addAll(list);
                verifyNextStep(month, year, transactions, categories, listener);
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });

        new CategoryFirebaseBusiness().findAll(new FirebaseDaoAbs.FirebaseMultiReturnListener<CategoryVO>() {
            @Override
            public void onFindAll(List<CategoryVO> list) {
                categories.addAll(list);
                verifyNextStep(month, year, transactions, categories, listener);
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });
    }

    private void verifyNextStep(final int month, final int year, List<TransactionVO> transactions, List<CategoryVO> categories, SingleResult listener) {
        if (!transactions.isEmpty() && !categories.isEmpty()) {
            listener.onFind(fillHomeObject(month, year, transactions, categories));
        }
    }


    private HomeObjectVO fillHomeObject(final int monthInt, final int yearInt, List<TransactionVO> transactions, List<CategoryVO> categories) {

        final HomeObjectVO home = new HomeObjectVO();
        home.setMonth(monthInt);
        home.setYear(yearInt);
        home.setListCategorySummary(new ArrayList<CategoryVO>());
        home.setListTransaction(transactions);
        home.setListMonth(new ArrayList<MonthVO>());

        for (TransactionVO transaction : transactions) {
            CategoryVO category = getCategoryIndex(categories, transaction, transaction.getCategory());
            if (!home.getListCategorySummary().contains(category)) {
                home.getListCategorySummary().add(category);
            } else {
                int index = home.getListCategorySummary().indexOf(category);
                home.getListCategorySummary().set(index, category);
            }

//            if (transaction.getCategorySecondary() != null) {
//                category = getCategoryIndex(categories, transaction, transaction.getCategorySecondary());
//                if (!home.getListCategorySummary().contains(category)) {
//                    home.getListCategorySummary().add(category);
//                } else {
//                    int index = home.getListCategorySummary().indexOf(category);
//                    home.getListCategorySummary().set(index, category);
//                }
//            }
        }

//        MonthVO month = new MonthVO();
//        month.setMonth(monthInt);
//        month.setYear(yearInt);
//        month.setValue(0D);
//
//        for (CategoryVO category : home.getListCategorySummary()) {
//            month.setValue(month.getValue() + category.getAmount());
//        }
//
//        List<MonthVO> months = new ArrayList<>();
//        months.add(month);
//
//        home.setListMonth(months);

        return home;
    }

    private CategoryVO getCategoryIndex(List<CategoryVO> categories, TransactionVO transaction, CategoryVO categoryParam) {

        CategoryVO category = categories.get(categories.indexOf(categoryParam));
        category.setAmount((category.getAmount() != null ? category.getAmount() : 0) + transaction.getValue());

        if (category.getTransactionList() == null) {
            category.setTransactionList(new ArrayList<TransactionVO>());
        }

        category.getTransactionList().add(transaction);
        return category;
    }

    /**
     *
     */
    public interface SingleResult {

        void onFind(HomeObjectVO homeObjectVO);

        void onError(String error);
    }

}
