package com.system.androidpigbank.models.firebase.business;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.system.androidpigbank.controllers.helpers.AppUtil;
import com.system.androidpigbank.controllers.helpers.Quinzena;
import com.system.androidpigbank.controllers.vos.CategoryVO;
import com.system.androidpigbank.controllers.vos.EndWhiteSpaceVO;
import com.system.androidpigbank.controllers.vos.EstimateVO;
import com.system.androidpigbank.controllers.vos.HomeObjectVO;
import com.system.androidpigbank.controllers.vos.MonthVO;
import com.system.androidpigbank.controllers.vos.TitleVO;
import com.system.androidpigbank.controllers.vos.TotalVO;
import com.system.androidpigbank.controllers.vos.TransactionVO;
import com.system.androidpigbank.controllers.vos.WhiteSpaceVO;
import com.system.architecture.activities.CardAdapterAbs;
import com.system.architecture.models.FirebaseAbs;
import com.system.architecture.utils.JavaUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Enir on 13/09/2016.
 * Classe usada para organizar os itens da home
 */

public class HomeBusiness {

    private List<TransactionVO> transactions;
    private List<CategoryVO> categories;
    private List<MonthVO> months;
    private List<EstimateVO> estimates;

    public void findAll(final int month, final int year, @NonNull final SingleResult listener) {

        new TransactionFirebaseBusiness().findTransactionByMonth(month, year, new FirebaseAbs.FirebaseMultiReturnListener<TransactionVO>() {
            @Override
            public void onFindAll(List<TransactionVO> list) {
                transactions = new ArrayList<>(list);
                verifyNextStep(month, year, transactions, categories, months, estimates, listener);
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });

        new CategoryFirebaseBusiness().findAll(new FirebaseAbs.FirebaseMultiReturnListener<CategoryVO>() {
            @Override
            public void onFindAll(List<CategoryVO> list) {
                categories = new ArrayList<>(list);
                verifyNextStep(month, year, transactions, categories, months, estimates, listener);
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });

        new MonthFirebaseBusiness().findAll(new FirebaseAbs.FirebaseMultiReturnListener<MonthVO>() {

            @Override
            public void onFindAll(List<MonthVO> list) {
                months = new ArrayList<>(list);
                verifyNextStep(month, year, transactions, categories, months, estimates, listener);
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });

        new EstimateFirebaseBusiness().findByMonth(month, year, new FirebaseAbs.FirebaseMultiReturnListener<EstimateVO>() {

            @Override
            public void onFindAll(List<EstimateVO> list) {
                estimates = new ArrayList<>(list);
                verifyNextStep(month, year, transactions, categories, months, estimates, listener);
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });
    }

    private void verifyNextStep(final int month, final int year, List<TransactionVO> transactions, List<CategoryVO> categories,
                                List<MonthVO> months, List<EstimateVO> estimates, SingleResult listener) {

        if (transactions != null && categories != null && months != null && estimates != null) {

            transactions = fillTransactions(categories, transactions);

            HomeObjectVO homeObjectVO = new HomeObjectVO();
            int index = months.indexOf(new MonthVO(month, year, 0D, 0D));

            if (index != -1) {
                homeObjectVO.setListCategorySummary(organizeCategorySummaryList(months.get(index), categories, transactions));
                homeObjectVO.setListTransaction(organizeTransationcList(transactions, categories));
                homeObjectVO.setListMonth(organizeMonthList(months));
                homeObjectVO.setListEstimate(organizeEstimates(estimates, categories, transactions));
                homeObjectVO.setCurrentMonth(getCurrentMonth(month, year, months, estimates));
            } else {
                homeObjectVO.setCurrentMonth(new MonthVO(month, year, 0D, 0D));
            }

            listener.onFind(homeObjectVO);
        }
    }

    private List<CardAdapterAbs.CardModel> organizeEstimates(List<EstimateVO> estimates, List<CategoryVO> categories, List<TransactionVO> transactions) {

        estimates.addAll(getNotEstimatedItems(transactions, estimates));

        List<CardAdapterAbs.CardModel> squad1Undated = new ArrayList<>();
        List<CardAdapterAbs.CardModel> squad2Undated = new ArrayList<>();

        Double sq1DSavedValue = 0D;
        Double sq1USavedValue = 0D;
        Double sq2DSavedValue = 0D;
        Double sq2USavedValue = 0D;

        Double sq1DValue = 0D;
        Double sq1UValue = 0D;
        Double sq2DValue = 0D;
        Double sq2UValue = 0D;

        Double sq1DValuePlann = 0D;
        Double sq1UValuePlann = 0D;
        Double sq2DValuePlann = 0D;
        Double sq2UValuePlann = 0D;

        for (EstimateVO vo : estimates) {

            for (CategoryVO cvo : categories) {

                if (vo.getCategory().getKey().equals(cvo.getKey())) {
                    vo.setCategory(cvo);
                } else if (vo.getCategorySecondary() != null && vo.getCategorySecondary().getKey().equals(cvo.getKey())) {
                    vo.setCategorySecondary(cvo);
                }

                if (!TextUtils.isEmpty(vo.getCategory().getName()) &&
                        (vo.getCategorySecondary() == null || !TextUtils.isEmpty(vo.getCategorySecondary().getName()))) {
                    break;
                }
            }
        }

        Collections.sort(estimates, new EstimateSort());

        for (EstimateVO vo : estimates) {

            vo.setSpentValue(0D);

            for (TransactionVO tvo : transactions) {

                if (tvo.isAlreadyEstimated()) {
                    continue;
                }

                int day = JavaUtils.DateUtil.get(Calendar.DATE, tvo.getDatePayment());

                // Se a categoria principal for igual
                if (vo.getCategory().equals(tvo.getCategory())) {

                    // se houverem categorias secundarias estimadas
                    if (vo.getCategorySecondary() != null) {

                        // se as categorias secundarias forem iguais
                        if (tvo.getCategorySecondary() != null && vo.getCategorySecondary().equals(tvo.getCategorySecondary())) {
                            setEstimatedValue(vo, tvo, day);
                        }

                    } else if (vo.getCategorySecondary() == null && tvo.getCategorySecondary() == null) {
                        setEstimatedValue(vo, tvo, day);
                    }
                }
            }

            vo.setSavedValue(vo.getPlannedValue() - vo.getSpentValue());
            vo.setPercentualVelue(JavaUtils.NumberUtil.calcPercent(vo.getPlannedValue(), vo.getSpentValue()));

            if (vo.getQuinzena().equals(Quinzena.PRIMEIRA)) {

                squad1Undated.add(vo);
                sq1UValue += vo.getSpentValue();
                sq1UValuePlann += vo.getPlannedValue();
                sq1USavedValue += vo.getSavedValue();

            } else {

                squad2Undated.add(vo);
                sq2UValue += vo.getSpentValue();
                sq2UValuePlann += vo.getPlannedValue();
                sq2USavedValue += vo.getSavedValue();
            }
        }

        List<CardAdapterAbs.CardModel> itens = new ArrayList<>();

        if (!squad1Undated.isEmpty()) {
            itens.add(new WhiteSpaceVO());
            itens.add(new TitleVO("Estimativa - 1a Quinzena"));
            itens.add(new WhiteSpaceVO());
            itens.addAll(squad1Undated);
            itens.add(new TotalVO(JavaUtils.NumberUtil.currencyFormat(sq1DValue + sq1UValue) + " de " + JavaUtils.NumberUtil.currencyFormat(sq1DValuePlann + sq1UValuePlann), JavaUtils.NumberUtil.currencyFormat(sq1DSavedValue + sq1USavedValue)));
        }

        if (!squad2Undated.isEmpty()) {
            itens.add(new WhiteSpaceVO());
            itens.add(new TitleVO("Estimativa - 2a Quinzena"));
            itens.add(new WhiteSpaceVO());
            itens.addAll(squad2Undated);
            itens.add(new TotalVO(JavaUtils.NumberUtil.currencyFormat(sq2DValue + sq2UValue) + " de " + JavaUtils.NumberUtil.currencyFormat(sq2DValuePlann + sq2UValuePlann), JavaUtils.NumberUtil.currencyFormat(sq2DSavedValue + sq2USavedValue)));
        }

        itens.add(new EndWhiteSpaceVO());
        return itens;
    }

    private void setEstimatedValue(EstimateVO vo, TransactionVO tvo, int day) {
        if ((vo.getQuinzena().equals(Quinzena.PRIMEIRA) && day < 20) || (vo.getQuinzena().equals(Quinzena.SEGUNDA) && day >= 20)) {
            vo.setSpentValue(vo.getSpentValue() + tvo.getValue());
            tvo.setAlreadyEstimated(true);
        }
    }

    private List<EstimateVO> getNotEstimatedItems(List<TransactionVO> transactions, List<EstimateVO> estimates) {


        Set<EstimateVO> notEstimated = new HashSet<>();

        for (TransactionVO tvo : transactions) {

            EstimateVO vo = new EstimateVO();
            vo.setCategory(tvo.getCategory());
            vo.setCategorySecondary(tvo.getCategorySecondary());
            vo.setQuinzena(AppUtil.getQuinzena(tvo.getDatePayment()));

            if (!estimates.contains(vo)) {
                vo.setRegistred(true);
                notEstimated.add(vo);
            }
        }

        return new ArrayList<>(notEstimated);
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

    private List<CardAdapterAbs.CardModel> organizeTransationcList(List<TransactionVO> transactions, List<CategoryVO> categories) {

        List<CardAdapterAbs.CardModel> itens = new ArrayList<>();

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

        itens.add(new EndWhiteSpaceVO());
        return itens;
    }

    private List<CardAdapterAbs.CardModel> organizeMonthList(List<MonthVO> months) {

        Collections.sort(months, new MonthSort());

        List<CardAdapterAbs.CardModel> itens = new ArrayList<>();
        itens.add(new WhiteSpaceVO());
        itens.addAll(months);
        itens.add(new WhiteSpaceVO());
        itens.add(new EndWhiteSpaceVO());
        return itens;
    }

    private MonthVO getCurrentMonth(int month, int year, List<MonthVO> months, List<EstimateVO> estimates) {

        Double estimate = 0D;
        for (EstimateVO vo : estimates) {
            estimate += vo.getPlannedValue();
        }

        MonthVO monthVO = months.get(months.indexOf(new MonthVO(month, year, 0D, 0D)));
        monthVO.setPlannedValue(estimate);
        return monthVO;
    }

    @NonNull
    private List<CardAdapterAbs.CardModel> organizeCategorySummaryList(MonthVO monthVO, List<CategoryVO> categories, List<TransactionVO> transactions) {

        boolean hasTitleSecondary = false;
        List<CardAdapterAbs.CardModel> itens = new ArrayList<>();

        for (int position = 0; position < categories.size(); position++) {

            CategoryVO category = categories.get(position);
            category.setAmount(0D);
            category.setMonthVO(monthVO);

            if (!hasTitleSecondary && !category.isPrimary()) {
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
        itens.add(new EndWhiteSpaceVO());
        return itens;
    }

    private List<CardAdapterAbs.CardModel> getTransactionByCategory(List<TransactionVO> transactions) {

        Double value = 0D;

        List<CardAdapterAbs.CardModel> itens = new ArrayList<>();
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
            int compareToYear = o1.getYear().compareTo(o2.getYear()) * -1;
            return compareToYear != 0 ? compareToYear : o1.getMonth().compareTo(o2.getMonth()) * -1;
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

    private class EstimateSort implements Comparator<EstimateVO> {

        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;

        @Override
        public int compare(EstimateVO o1, EstimateVO o2) {

            int compare = Integer.compare(o1.getQuinzena().ordinal(), o2.getQuinzena().ordinal());
            if (compare != EQUAL) return compare;

            compare = o1.getCategory().getName().compareTo(o2.getCategory().getName());
            if (compare != EQUAL) return compare;

            if (o1.getCategorySecondary() == null) return BEFORE;
            if (o2.getCategorySecondary() == null) return AFTER;
            return o1.getCategorySecondary().getName().compareTo(o2.getCategorySecondary().getName());
        }
    }
}
