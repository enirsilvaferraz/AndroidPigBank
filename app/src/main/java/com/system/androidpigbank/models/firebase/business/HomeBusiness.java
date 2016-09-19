package com.system.androidpigbank.models.firebase.business;

import android.support.annotation.NonNull;

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
            listener.onFind(fillHomeObjectV2(month, year, transactions, categories, months));
        }
    }

    private HomeObjectVO fillHomeObjectV2(final int monthInt, final int yearInt, List<TransactionVO> transactions, List<CategoryVO> categories, List<MonthVO> months) {

        final MonthVO month = new MonthVO();
        month.setMonth(monthInt);
        month.setYear(yearInt);
        month.setValue(0D);

        final HomeObjectVO home = new HomeObjectVO();
        home.setMonth(monthInt);
        home.setYear(yearInt);

        for (TransactionVO tvo : transactions) {
            CategoryVO cvo = categories.get(categories.indexOf(tvo.getCategory()));
            cvo.setAmount(cvo.getAmount() + tvo.getValue());
            tvo.setCategory(cvo);
            cvo.getTransactionList().add(tvo);

            if (tvo.getCategorySecondary() != null) {
                CategoryVO cvo2 = categories.get(categories.indexOf(tvo.getCategorySecondary()));
                cvo2.setAmount(cvo2.getAmount() + tvo.getValue());
                tvo.setCategorySecondary(cvo2);
                cvo2.getTransactionList().add(tvo);

                Collections.sort(cvo2.getTransactionList(), new TransactionSort());
            }

            Collections.sort(cvo.getTransactionList(), new TransactionSort());
            month.setValue(month.getValue() + tvo.getValue());
        }

        home.setListTransaction(transactions);
        home.setListCategorySummary(categories);

        if (!months.contains(month)) {
            months.add(new MonthFirebaseBusiness().save(month));
        } else {
            int index = months.indexOf(month);
            MonthVO monthVO = months.get(index);
            if (!monthVO.getValue().equals(month.getValue())) {
                monthVO.setValue(month.getValue());
                months.set(index, new MonthFirebaseBusiness().save(monthVO));
            }
        }

        Collections.sort(months, new MonthSort());

        home.setListMonth(months);
        return home;
    }

    private class MonthSort implements Comparator<MonthVO> {

        @Override
        public int compare(MonthVO o1, MonthVO o2) {
            int compareToYear = o1.getYear().compareTo(o2.getYear());
            return compareToYear != 0 ? compareToYear : o1.getMonth().compareTo(o2.getMonth());
        }
    }

    private class TransactionSort implements Comparator<TransactionVO> {

        @Override
        public int compare(TransactionVO o1, TransactionVO o2) {
            if (o1.getCategorySecondary() == null) {
                return -1;
            } else if (o2.getCategorySecondary() == null) {
                return 1;
            } else {
                return o1.getCategorySecondary().getName().compareTo(o2.getCategorySecondary().getName());
            }
        }
    }

    private class CategorySort implements Comparator<CategoryVO> {
        @Override
        public int compare(CategoryVO o1, CategoryVO o2) {
            int compare = Boolean.compare(o1.isPrimary(), o2.isPrimary());
            return compare != 0 ? compare : o1.getName().compareTo(o2.getName());
        }
    }

    /**
     *
     */
    public interface SingleResult {

        void onFind(HomeObjectVO homeObjectVO);

        void onError(String error);
    }

}
