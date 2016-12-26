package com.system.androidpigbank.models.firebase.business;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.system.androidpigbank.BuildConfig;
import com.system.androidpigbank.controllers.vos.CategoryVO;
import com.system.androidpigbank.controllers.vos.EstimateVO;
import com.system.androidpigbank.controllers.vos.TransactionVO;
import com.system.androidpigbank.models.firebase.dtos.EstimateDTO;
import com.system.architecture.models.DTOAbs;
import com.system.architecture.models.FirebaseAbs;
import com.system.architecture.utils.JavaUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}