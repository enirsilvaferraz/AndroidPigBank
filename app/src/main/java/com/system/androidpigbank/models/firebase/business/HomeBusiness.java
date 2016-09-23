package com.system.androidpigbank.models.firebase.business;

import android.support.annotation.NonNull;

import com.system.androidpigbank.controllers.vos.CategoryVO;
import com.system.androidpigbank.controllers.vos.HomeObjectVO;
import com.system.androidpigbank.controllers.vos.MonthVO;
import com.system.androidpigbank.controllers.vos.TitleVO;
import com.system.androidpigbank.controllers.vos.TotalVO;
import com.system.androidpigbank.controllers.vos.TransactionVO;
import com.system.androidpigbank.controllers.vos.WhiteSpaceVO;
import com.system.architecture.adapters.CardAdapter;
import com.system.architecture.utils.JavaUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Enir on 13/09/2016.
 * Classe usada para organizar os itens da home
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

            transactions = fillTransactions(categories, transactions);

            HomeObjectVO homeObjectVO = new HomeObjectVO(); //fillHomeObjectV2(month, year, transactions, categories, months);
            homeObjectVO.setMonth(month);
            homeObjectVO.setYear(year);
            homeObjectVO.setListCategorySummary(organizeCategorySummaryList(categories, transactions));
            homeObjectVO.setListTransaction(organizeTransationcList(transactions, categories));
            homeObjectVO.setListMonth(organizeList(month, year, months));
            listener.onFind(homeObjectVO);
        }
    }

    private List<TransactionVO> fillTransactions(List<CategoryVO> categories, List<TransactionVO> transactions) {

        for (TransactionVO tvo : transactions) {
            tvo.setCategory(categories.get(categories.indexOf(tvo.getCategory())));
            if (tvo.getCategorySecondary() != null) {
                tvo.setCategorySecondary(categories.get(categories.indexOf(tvo.getCategorySecondary())));
            }
        }

        return transactions;
    }

    private List<CardAdapter.CardModel> organizeTransationcList(List<TransactionVO> transactions, List<CategoryVO> categories) {

        List<CardAdapter.CardModel> itens = new ArrayList<>();

        Double valorAcumular = 0D;
        boolean hasTitleFutureLanc = false;

        itens.add(new WhiteSpaceVO());

        for (int position = 0; position < transactions.size(); position++) {

            // Define a transacao atual e proxima
            TransactionVO transactionAct = transactions.get(position);
            TransactionVO transactionProx = transactions.size() > position + 1 ? transactions.get(position + 1) : null;

            // Identifica a categoria da transacao atual
            transactionAct.setCategory(categories.get(categories.indexOf(transactionAct.getCategory())));

            // Calcula o valor a acumular das transacoes futuras
            if (JavaUtils.DateUtil.compare(transactionAct.getDatePayment(), Calendar.getInstance().getTime()) == 0) {
                valorAcumular += transactionAct.getValue();
            } else if (JavaUtils.DateUtil.compare(transactionAct.getDatePayment(), Calendar.getInstance().getTime()) > 0) {
                if (!hasTitleFutureLanc) {
                    itens.add(new TitleVO("Lançamentos Futuros"));
                    itens.add(new WhiteSpaceVO());
                    hasTitleFutureLanc = true;
                }
                valorAcumular += transactionAct.getValue();
            }

            // Adiciona a transacao a lista
            itens.add(transactionAct);

            // Adiciona o total
            if (transactionProx == null || JavaUtils.DateUtil.compare(transactionAct.getDatePayment(), transactionProx.getDatePayment()) != 0) {
                itens.add(new TotalVO(null, valorAcumular));
                itens.add(new WhiteSpaceVO());
            }
        }

        return itens;
    }

    private List<CardAdapter.CardModel> organizeList(final int monthInt, final int yearInt, List<MonthVO> months) {

        saveMonth(monthInt, yearInt, months);

        Collections.sort(months, new MonthSort());

        List<CardAdapter.CardModel> list = new ArrayList<>();
        list.add(new WhiteSpaceVO());

        int year = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 11; i >= 0; i--) {
            MonthVO month = new MonthVO();
            month.setYear(year);
            month.setMonth(i);
            month.setValue(0D);

            if (months.contains(month)) {
                month = months.get(months.indexOf(month));
            }

            list.add(month);
        }

        list.add(new WhiteSpaceVO());
        return list;
    }

    private void saveMonth(int monthInt, int yearInt, List<MonthVO> months) {
        final MonthVO month = new MonthVO();
        month.setMonth(monthInt);
        month.setYear(yearInt);
        month.setValue(0D);

        for (TransactionVO tvo : transactions) {
            month.setValue(month.getValue() + tvo.getValue());
        }

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
    }

    @NonNull
    private List<CardAdapter.CardModel> organizeCategorySummaryList(List<CategoryVO> categories, List<TransactionVO> transactions) {

        boolean hasTitleSecondary = false;
        List<CardAdapter.CardModel> itens = new ArrayList<>();

        for (int position = 0; position < categories.size(); position++) {

            CategoryVO category = categories.get(position);
            category.setAmount(0D);

            if (!hasTitleSecondary && !category.isPrimary() && position != categories.size() - 1) {
                itens.add(new WhiteSpaceVO());
                itens.add(new TitleVO("Secondary Categories"));
                hasTitleSecondary = true;
            }

            itens.add(new WhiteSpaceVO());
            itens.add(category);

            List<TransactionVO> listVo = new ArrayList<>();
            for (TransactionVO tvo : transactions) {

                if (category.getKey().equals(tvo.getCategory().getKey())) {
                    listVo.add(tvo);
                    category.setAmount(category.getAmount() + tvo.getValue());
                }

                if (tvo.getCategorySecondary() != null && category.getKey().equals(tvo.getCategorySecondary().getKey())) {
                    listVo.add(tvo);
                    category.setAmount(category.getAmount() + tvo.getValue());
                }
            }

            if (!listVo.isEmpty()) {
                Collections.sort(listVo, new TransactionSort());
                itens.addAll(getTransactionByCategory(listVo));
            }
        }

        itens.add(new WhiteSpaceVO());

        return itens;
    }

    private List<CardAdapter.CardModel> getTransactionByCategory(List<TransactionVO> transactions) {

        Double value = 0D;

        List<CardAdapter.CardModel> itens = new ArrayList<>();
        for (int position = 0; position < transactions.size(); position++) {

            TransactionVO transactionAct = transactions.get(position);
            TransactionVO transactionProx = transactions.size() > position + 1 ? transactions.get(position + 1) : null;

            String categoryAct = transactionAct.getCategorySecondary() != null ? transactionAct.getCategorySecondary().getName() : "";
            String categoryProx = transactionProx != null && transactionProx.getCategorySecondary() != null ? transactionProx.getCategorySecondary().getName() : "";

            itens.add(transactionAct);
            value += transactionAct.getValue();

            if (transactionProx == null || !categoryAct.equals(categoryProx)) {
                itens.add(new TotalVO(null, value));
                value = 0D;
            }
        }

        return itens;
    }

    /**
     *
     */
    public interface SingleResult {

        void onFind(HomeObjectVO homeObjectVO);

        void onError(String error);
    }

    /**
     *
     */
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
}
