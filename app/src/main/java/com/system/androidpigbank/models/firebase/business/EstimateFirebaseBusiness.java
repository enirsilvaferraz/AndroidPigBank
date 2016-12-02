package com.system.androidpigbank.models.firebase.business;

import com.system.androidpigbank.BuildConfig;
import com.system.androidpigbank.controllers.vos.CategoryVO;
import com.system.androidpigbank.controllers.vos.EstimateVO;
import com.system.androidpigbank.controllers.vos.TransactionVO;
import com.system.androidpigbank.models.firebase.dtos.EstimateDTO;
import com.system.architecture.models.DTOAbs;
import com.system.architecture.models.FirebaseAbs;

import java.util.HashMap;
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

        return map;
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