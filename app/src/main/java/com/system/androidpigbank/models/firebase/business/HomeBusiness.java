package com.system.androidpigbank.models.firebase.business;

import android.support.annotation.NonNull;
import android.util.Log;

import com.system.androidpigbank.controllers.vos.CategoryVO;
import com.system.androidpigbank.controllers.vos.HomeObjectVO;
import com.system.androidpigbank.controllers.vos.MonthVO;
import com.system.androidpigbank.controllers.vos.TransactionVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Enir on 13/09/2016.
 */

public class HomeBusiness {

    private List<TransactionVO> transactions;
    private List<CategoryVO> categories;
    private List<MonthVO> months;

    public void findAll(final int month, final int year, @NonNull final SingleResult listener) {

        new TransactionFirebaseBusiness().findTransactionByMonth(month, year, new FirebaseDaoAbs.FirebaseMultiReturnListener<TransactionVO>() {
            @Override
            public void onFindAll(List<TransactionVO> list) {
                transactions = new ArrayList<>(list);
                verifyNextStep(month, year, transactions, categories, months, listener);
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });

        new CategoryFirebaseBusiness().findAll(new FirebaseDaoAbs.FirebaseMultiReturnListener<CategoryVO>() {
            @Override
            public void onFindAll(List<CategoryVO> list) {
                categories = new ArrayList<>(list);
                verifyNextStep(month, year, transactions, categories, months, listener);
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });

        new MonthFirebaseBusiness().findAll(new FirebaseDaoAbs.FirebaseMultiReturnListener<MonthVO>() {

            @Override
            public void onFindAll(List<MonthVO> list) {
                months = new ArrayList<>(list);
                verifyNextStep(month, year, transactions, categories, months, listener);
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });
    }

    private void verifyNextStep(final int month, final int year, List<TransactionVO> transactions, List<CategoryVO> categories, List<MonthVO> months, SingleResult listener) {
        if (transactions != null && categories != null && months != null) {
            listener.onFind(fillHomeObject(month, year, transactions, categories, months));
        }
    }


    private HomeObjectVO fillHomeObject(final int monthInt, final int yearInt, List<TransactionVO> transactions, List<CategoryVO> categories, List<MonthVO> months) {

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

        Collections.sort(home.getListCategorySummary(), new Comparator<CategoryVO>() {
            @Override
            public int compare(CategoryVO o1, CategoryVO o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });


        Double total = 0D;
        for (CategoryVO category : home.getListCategorySummary()) {
            total += category.getAmount();

            Collections.sort(category.getTransactionList(), new Comparator<TransactionVO>() {
                @Override
                public int compare(TransactionVO o1, TransactionVO o2) {

                    if (o1.getCategorySecondary() == null){
                        return -1;
                    } else if (o2.getCategorySecondary() == null) {
                        return 1;
                    } else {
                        return o1.getCategorySecondary().getName().compareTo(o2.getCategorySecondary().getName());
                    }
                }
            });
        }

        MonthVO month = new MonthVO();
        month.setMonth(monthInt);
        month.setYear(yearInt);

        if (!months.contains(month)) {
            month.setValue(total);
            months.add(new MonthFirebaseBusiness().save(month));
        } else {
            int index = months.indexOf(month);
            MonthVO monthVO = months.get(index);
            if (!monthVO.getValue().equals(total)) {
                monthVO.setValue(total);
                new MonthFirebaseBusiness().save(monthVO);
                months.set(index, monthVO);
            }
        }

        home.setListMonth(months);

        Collections.sort(months, new Comparator<MonthVO>() {
            @Override
            public int compare(MonthVO o1, MonthVO o2) {
                int compareToYear = o1.getYear().compareTo(o2.getYear());
                return compareToYear != 0 ? compareToYear : o1.getMonth().compareTo(o2.getMonth());
            }
        });

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
