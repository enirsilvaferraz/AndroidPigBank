package com.system.androidpigbank.models.firebase.business;

import com.system.androidpigbank.controllers.vos.CategoryVO;
import com.system.androidpigbank.models.firebase.dtos.CategoryDTO;
import com.system.androidpigbank.models.firebase.dtos.DTOAbs;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Enir on 08/09/2016.
 * Business de Category
 */

public class CategoryFirebaseBusiness extends FirebaseDaoAbs<CategoryVO> {

    @Override
    protected Map<String, Object> pupulateMap(CategoryVO vo) {

        CategoryDTO dto = (CategoryDTO) vo.toDTO();

        Map<String, Object> map = new HashMap<>();
        map.put("name", dto.getName());
        map.put("primary", dto.isPrimary());

        return map;
    }

    @Override
    protected Class<? extends DTOAbs> getDTOClass() {
        return CategoryDTO.class;
    }
}