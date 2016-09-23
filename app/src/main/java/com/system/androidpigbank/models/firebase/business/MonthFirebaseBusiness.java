package com.system.androidpigbank.models.firebase.business;

import com.system.androidpigbank.controllers.vos.MonthVO;
import com.system.androidpigbank.models.firebase.dtos.DTOAbs;
import com.system.androidpigbank.models.firebase.dtos.MonthDTO;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Enir on 08/09/2016.
 * Business de Month
 */

class MonthFirebaseBusiness extends FirebaseDaoAbs<MonthVO> {

    @Override
    protected Class<? extends DTOAbs> getDTOClass() {
        return MonthDTO.class;
    }

    @Override
    protected Map<String, Object> pupulateMap(MonthVO vo) {

        MonthDTO dto = (MonthDTO) vo.toDTO();

        Map<String, Object> map = new HashMap<>();
        map.put("month", dto.getMonth());
        map.put("year", dto.getYear());
        map.put("value", dto.getValue());

        return map;
    }
}