package com.system.androidpigbank.models.firebase.routines;

import android.util.Log;

import com.system.androidpigbank.controllers.vos.EstimateVO;
import com.system.androidpigbank.models.firebase.business.EstimateFirebaseBusiness;
import com.system.architecture.models.FirebaseAbs;
import com.system.architecture.utils.JavaUtils;

import java.util.Calendar;
import java.util.List;

/**
 * Created by eferraz on 11/12/16.
 */

public class RoutineService {

    public static void updateEstimates() {

        new EstimateFirebaseBusiness().findAll(new FirebaseAbs.FirebaseMultiReturnListener<EstimateVO>() {

            @Override
            public void onFindAll(List<EstimateVO> list) {

                for (EstimateVO vo : list) {

                    if (vo.getDate() != null){
                        continue;
                    }

                    vo.setMonth(null);
                    vo.setYear(null);

                    String format = JavaUtils.DateUtil.format(Calendar.getInstance().getTime(), JavaUtils.DateUtil.MM_YYYY);
                    vo.setDate(JavaUtils.DateUtil.parse(format, JavaUtils.DateUtil.MM_YYYY));

                    new EstimateFirebaseBusiness().save(vo, new FirebaseAbs.FirebaseSingleReturnListener<EstimateVO>() {

                        @Override
                        public void onFind(EstimateVO list) {
                            Log.i(RoutineService.class.getSimpleName(), list.toString());
                        }

                        @Override
                        public void onError(String error) {
                            Log.e(RoutineService.class.getSimpleName(), error);
                        }
                    });
                }
            }

            @Override
            public void onError(String error) {
                Log.e(RoutineService.class.getSimpleName(), error);
            }
        });
    }
}
