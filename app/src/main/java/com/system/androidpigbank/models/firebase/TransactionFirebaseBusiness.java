package com.system.androidpigbank.models.firebase;


import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.system.androidpigbank.models.firebase.dtos.DTOAbs;
import com.system.androidpigbank.models.firebase.dtos.TransactionDTO;
import com.system.androidpigbank.models.sqlite.entities.Transaction;
import com.system.architecture.utils.JavaUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Enir on 08/09/2016.
 */

public class TransactionFirebaseBusiness extends FirebaseDaoAbs<Transaction> {

    @Override
    protected Class<? extends DTOAbs> getDTOClass() {
        return TransactionDTO.class;
    }

    public void findTransactionByMonth(int month, int year, @NonNull final FirebaseMultiReturnListener listener) {

        Date cInit = JavaUtils.DateUtil.getActualMaximum(year, month - 1);
        Date cEnd = JavaUtils.DateUtil.getActualMaximum(year, month);

        getDatabaseReference().orderByChild("datePayment")
                .startAt(JavaUtils.DateUtil.format(cInit, JavaUtils.DateUtil.YYYY_MM_DD))
                .endAt(JavaUtils.DateUtil.format(cEnd, JavaUtils.DateUtil.YYYY_MM_DD))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Transaction> list = new ArrayList<>();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            list.add(getTInstance(postSnapshot));
                        }
                        listener.onFindAll(list);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listener.onError(databaseError.getMessage());
                    }
                });
    }
}