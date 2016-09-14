package com.system.androidpigbank.models.firebase;

import com.system.androidpigbank.models.firebase.dtos.CategoryDTO;
import com.system.androidpigbank.models.firebase.dtos.DTOAbs;
import com.system.androidpigbank.models.sqlite.entities.Category;

/**
 * Created by Enir on 08/09/2016.
 */

public class MonthFirebaseBusiness extends FirebaseDaoAbs<Category> {
    @Override
    protected Class<? extends DTOAbs> getDTOClass() {
        return CategoryDTO.class;
    }
}