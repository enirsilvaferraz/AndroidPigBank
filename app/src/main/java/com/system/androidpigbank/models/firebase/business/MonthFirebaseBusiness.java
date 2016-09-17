package com.system.androidpigbank.models.firebase.business;

import com.system.androidpigbank.controllers.vos.MonthVO;
import com.system.androidpigbank.controllers.vos.WhiteSpaceVO;
import com.system.androidpigbank.models.firebase.dtos.DTOAbs;
import com.system.androidpigbank.models.firebase.dtos.MonthDTO;
import com.system.architecture.adapters.CardAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Enir on 08/09/2016.
 */

public class MonthFirebaseBusiness extends FirebaseDaoAbs<MonthVO> {

    @Override
    protected Class<? extends DTOAbs> getDTOClass() {
        return MonthDTO.class;
    }

    public List<CardAdapter.CardModel> organizeList(List<MonthVO> listMonth) {
        List<CardAdapter.CardModel> list = new ArrayList<>();
        list.add(new WhiteSpaceVO());

        int year = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 11; i >= 0; i--) {
            MonthVO month = new MonthVO();
            month.setYear(year);
            month.setMonth(i);
            month.setValue(0D);

            if (listMonth.contains(month)) {
                month = listMonth.get(listMonth.indexOf(month));
            }

            list.add(month);
        }

        list.add(new WhiteSpaceVO());
        return list;
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