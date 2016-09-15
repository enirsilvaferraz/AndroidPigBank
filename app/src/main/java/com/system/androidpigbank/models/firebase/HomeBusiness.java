package com.system.androidpigbank.models.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.system.androidpigbank.BuildConfig;
import com.system.androidpigbank.controllers.vos.HomeObjectVO;
import com.system.androidpigbank.controllers.vos.Month;
import com.system.androidpigbank.models.sqlite.entities.Category;
import com.system.androidpigbank.models.sqlite.entities.Transaction;

import java.util.List;

/**
 * Created by Enir on 13/09/2016.
 */

public class HomeBusiness {

    public void findAll(final int month, int year, @NonNull final SingleResult listener) {

        final HomeObjectVO homeVO = new HomeObjectVO();
        homeVO.setMonth(month);
        homeVO.setYear(year);

        new TransactionFirebaseBusiness().findTransactionByMonth(month, year, new FirebaseDaoAbs.FirebaseMultiReturnListener<Transaction>() {
            @Override
            public void onFindAll(List<Transaction> list) {

                homeVO.setListTransaction(list);

                new CategoryFirebaseBusiness().findAll(new FirebaseDaoAbs.FirebaseMultiReturnListener<Category>() {
                    @Override
                    public void onFindAll(List<Category> list) {

                        homeVO.setListCategorySummary(list);

                        new MonthFirebaseBusiness().findAll(new FirebaseDaoAbs.FirebaseMultiReturnListener<Month>() {
                            @Override
                            public void onFindAll(List<Month> list) {

                                homeVO.setListMonth(list);
                                prepareList(homeVO);
                                listener.onFind(homeVO);
                            }

                            @Override
                            public void onError(String error) {
                                listener.onError(error);
                            }
                        });
                    }

                    @Override
                    public void onError(String error) {
                        listener.onError(error);
                    }
                });

            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });

    }

    private void prepareList(HomeObjectVO homeVO) {

    }

    /**
     *
     */
    public interface SingleResult {

        void onFind(HomeObjectVO vo);

        void onError(String error);
    }
}
