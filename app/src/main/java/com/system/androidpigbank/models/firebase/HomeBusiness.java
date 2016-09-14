package com.system.androidpigbank.models.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.system.androidpigbank.controllers.vos.HomeObjectVO;
import com.system.androidpigbank.models.sqlite.entities.Transaction;

import java.util.List;

/**
 * Created by Enir on 13/09/2016.
 */

public class HomeBusiness {

    public void findAll(int month, int year, @NonNull final SingleResult listener) {

        final HomeObjectVO homeVO = new HomeObjectVO();
        homeVO.setMonth(month);
        homeVO.setYear(year);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("tst-database");


    }

    /**
     *
     */
    public interface SingleResult {

        void onFind(HomeObjectVO vo);

        void onError(String error);
    }
}
