package com.system.androidpigbank.models.firebase.business;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.system.androidpigbank.BuildConfig;
import com.system.androidpigbank.controllers.helpers.AppUtil;
import com.system.androidpigbank.controllers.helpers.Quinzena;
import com.system.androidpigbank.controllers.vos.CategoryVO;
import com.system.androidpigbank.controllers.vos.EstimateVO;
import com.system.androidpigbank.controllers.vos.TransactionVO;
import com.system.androidpigbank.models.firebase.dtos.EstimateDTO;
import com.system.architecture.models.DTOAbs;
import com.system.architecture.models.FirebaseAbs;
import com.system.architecture.utils.JavaUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Enir on 08/09/2016.
 * Business de Category
 */

public class EstimateFirebaseBusiness extends FirebaseAbs<EstimateVO> {

    public EstimateFirebaseBusiness() {
        super(BuildConfig.FLAVOR);
    }

    @Override
    protected Map<String, Object> pupulateMap(EstimateVO vo) {

        EstimateDTO dto = (EstimateDTO) vo.toDTO();

        Map<String, Object> map = new HashMap<>();
        map.put("category", dto.getCategory());
        map.put("categorySecondary", dto.getCategorySecondary());
        map.put("day", dto.getDay());
        map.put("plannedValue", dto.getPlannedValue());
        map.put("quinzena", dto.getQuinzena());
        map.put("month", dto.getMonth());
        map.put("year", dto.getYear());
        map.put("date", dto.getDate());

        return map;
    }

    void findByMonth(int month, int year, @NonNull final FirebaseMultiReturnListener<EstimateVO> listener) {

        Date cInit = JavaUtils.DateUtil.getActualMinimum(year, month);
        Date cEnd = JavaUtils.DateUtil.getActualMaximum(year, month);

        ValueEventListener valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<EstimateVO> list = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    EstimateVO tInstance = getTInstance(postSnapshot);
                    tInstance.setKey(postSnapshot.getKey());
                    list.add(tInstance);
                }
                listener.onFindAll(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        };

        getDatabaseReference().orderByChild("date")
                .startAt(JavaUtils.DateUtil.format(cInit, JavaUtils.DateUtil.MM_YYYY))
                .endAt(JavaUtils.DateUtil.format(cEnd, JavaUtils.DateUtil.MM_YYYY))
                .addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    protected Class<? extends DTOAbs> getDTOClass() {
        return EstimateDTO.class;
    }

    @Override
    public void save(EstimateVO entity, FirebaseSingleReturnListener<EstimateVO> listener) {
        saveCategory(entity, listener);
    }

    private void saveCategory(final EstimateVO entity, final FirebaseSingleReturnListener<EstimateVO> listener) {

        if (entity.getCategory().getKey() == null) {

            final CategoryVO category = entity.getCategory();
            category.setPrimary(true);

            new CategoryFirebaseBusiness().save(category, new FirebaseSingleReturnListener<CategoryVO>() {

                @Override
                public void onFind(CategoryVO list) {
                    saveSecondary(entity.getCategorySecondary(), entity, listener);
                }

                @Override
                public void onError(String error) {
                    listener.onError(error);
                }
            });
        } else {
            saveSecondary(entity.getCategorySecondary(), entity, listener);
        }
    }

    private void saveSecondary(CategoryVO category, final EstimateVO entity, final FirebaseSingleReturnListener<EstimateVO> listener) {

        if (entity.getCategorySecondary() != null && entity.getCategorySecondary().getKey() == null) {

            new CategoryFirebaseBusiness().save(category, new FirebaseSingleReturnListener<CategoryVO>() {

                @Override
                public void onFind(CategoryVO list) {
                    EstimateFirebaseBusiness.super.save(entity, listener);
                }

                @Override
                public void onError(String error) {
                    listener.onError(error);
                }
            });
        } else {
            EstimateFirebaseBusiness.super.save(entity, listener);
        }
    }


    static List<EstimateVO> populateValues(List<EstimateVO> estimates, List<CategoryVO> categories, List<TransactionVO> transactions) {

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

                // Se a categoria principal for igual
                if (vo.getCategory().equals(tvo.getCategory())) {

                    // se houverem categorias secundarias estimadas
                    if (vo.getCategorySecondary() != null) {

                        // se as categorias secundarias forem iguais
                        if (tvo.getCategorySecondary() != null && vo.getCategorySecondary().equals(tvo.getCategorySecondary())) {
                            setEstimatedValue(vo, tvo);
                        }

                    } else if (vo.getCategorySecondary() == null && tvo.getCategorySecondary() == null) {
                        setEstimatedValue(vo, tvo);
                    }
                }
            }

            vo.setSavedValue(vo.getPlannedValue() - vo.getSpentValue());
            vo.setPercentualVelue(JavaUtils.NumberUtil.calcPercent(vo.getPlannedValue(), vo.getSpentValue()));
        }

        return estimates;
    }

    private static void setEstimatedValue(EstimateVO vo, TransactionVO tvo) {
        int day = JavaUtils.DateUtil.get(Calendar.DATE, tvo.getDatePayment());
        if ((vo.getQuinzena().equals(Quinzena.PRIMEIRA) && day < 20) || (vo.getQuinzena().equals(Quinzena.SEGUNDA) && day >= 20)) {
            vo.setSpentValue(vo.getSpentValue() + tvo.getValue());
            tvo.setAlreadyEstimated(true);
        }
    }

    static List<EstimateVO> getNotEstimatedItems(List<TransactionVO> transactions, List<EstimateVO> estimates) {


        Set<EstimateVO> notEstimated = new HashSet<>();

        for (TransactionVO tvo : transactions) {

            EstimateVO vo = new EstimateVO();
            vo.setCategory(tvo.getCategory());
            vo.setCategorySecondary(tvo.getCategorySecondary());
            vo.setQuinzena(AppUtil.getQuinzena(tvo.getDatePayment()));

            if (!estimates.contains(vo)) {
                vo.setAuxItem(true);
                notEstimated.add(vo);
            }
        }

        return new ArrayList<>(notEstimated);
    }

    private static class EstimateSort implements Comparator<EstimateVO> {

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