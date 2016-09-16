package com.system.androidpigbank.models.firebase;

import com.system.androidpigbank.controllers.vos.MonthVO;
import com.system.androidpigbank.models.firebase.dtos.DTOAbs;
import com.system.androidpigbank.models.firebase.dtos.MonthDTO;

import java.util.Map;

/**
 * Created by Enir on 08/09/2016.
 */

public class MonthFirebaseBusiness extends FirebaseDaoAbs<MonthVO> {

    @Override
    protected Map<String, Object> pupulateMap(MonthVO vo) {
        return null;
    }

    @Override
    protected Class<? extends DTOAbs> getDTOClass() {
        return MonthDTO.class;
    }
}