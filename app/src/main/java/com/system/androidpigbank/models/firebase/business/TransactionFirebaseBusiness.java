package com.system.androidpigbank.models.firebase.business;


import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.system.androidpigbank.controllers.vos.CategoryVO;
import com.system.androidpigbank.controllers.vos.MonthVO;
import com.system.androidpigbank.controllers.vos.TransactionVO;
import com.system.androidpigbank.models.firebase.dtos.DTOAbs;
import com.system.androidpigbank.models.firebase.dtos.TransactionDTO;
import com.system.architecture.managers.EntityAbs;
import com.system.architecture.utils.JavaUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Enir on 08/09/2016.
 * Business de Transaction
 */

public class TransactionFirebaseBusiness extends FirebaseDaoAbs<TransactionVO> {

    public void save(final TransactionVO transaction, final FirebaseSingleReturnListener<TransactionVO> listener) {

        new MonthFirebaseBusiness().update(transaction, new FirebaseSingleReturnListener<MonthVO>() {

            @Override
            public void onFind(MonthVO list) {
                saveCategory(transaction, listener);
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });
    }

    private void saveCategory(final TransactionVO transaction, final FirebaseSingleReturnListener<TransactionVO> listener) {

        if (transaction.getCategory().getKey() == null) {

            final CategoryVO category = transaction.getCategory();
            category.setPrimary(true);

            new CategoryFirebaseBusiness().save(category, new FirebaseSingleReturnListener<CategoryVO>() {

                @Override
                public void onFind(CategoryVO list) {
                    saveSecondary(transaction.getCategorySecondary(), transaction, listener);
                }

                @Override
                public void onError(String error) {
                    listener.onError(error);
                }
            });
        } else {
            saveSecondary(transaction.getCategorySecondary(), transaction, listener);
        }
    }

    private void saveSecondary(CategoryVO category, final TransactionVO transaction, final FirebaseSingleReturnListener<TransactionVO> listener) {

        if (transaction.getCategorySecondary() != null && transaction.getCategorySecondary().getKey() == null) {

            new CategoryFirebaseBusiness().save(category, new FirebaseSingleReturnListener<CategoryVO>() {

                @Override
                public void onFind(CategoryVO list) {
                    TransactionFirebaseBusiness.super.save(transaction, listener);
                }

                @Override
                public void onError(String error) {
                    listener.onError(error);
                }
            });
        } else {
            TransactionFirebaseBusiness.super.save(transaction, listener);
        }
    }

    @Override
    protected Map<String, Object> pupulateMap(TransactionVO vo) {

        TransactionDTO dto = (TransactionDTO) vo.toDTO();

        Map<String, Object> map = new HashMap<>();
        map.put("value", dto.getValue());
        map.put("category", dto.getCategory());
        map.put("categorySecondary", dto.getCategorySecondary());
        map.put("content", dto.getContent());
        map.put("datePayment", dto.getDatePayment());
        map.put("dateTransaction", dto.getDateTransaction());
        map.put("paymentType", dto.getPaymentType());

        return map;
    }

    @Override
    public void delete(final TransactionVO entity, final FirebaseSingleReturnListener listener) {

        new MonthFirebaseBusiness().delete(entity, new FirebaseSingleReturnListener() {
            @Override
            public void onFind(EntityAbs list) {
                TransactionFirebaseBusiness.super.delete(entity, listener);
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });
    }

    @Override
    protected Class<? extends DTOAbs> getDTOClass() {
        return TransactionDTO.class;
    }

    void findTransactionByMonth(int month, int year, @NonNull final FirebaseMultiReturnListener<TransactionVO> listener) {

        Date cInit = JavaUtils.DateUtil.getActualMinimum(year, month);
        Date cEnd = JavaUtils.DateUtil.getActualMaximum(year, month);

        ValueEventListener valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<TransactionVO> list = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    TransactionVO tInstance = getTInstance(postSnapshot);
                    tInstance.setKey(postSnapshot.getKey());
                    list.add(tInstance);
                }
                listener.onFindAll(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        };

        getDatabaseReference().orderByChild("datePayment")
                .startAt(JavaUtils.DateUtil.format(cInit, JavaUtils.DateUtil.YYYY_MM_DD))
                .endAt(JavaUtils.DateUtil.format(cEnd, JavaUtils.DateUtil.YYYY_MM_DD))
                .addListenerForSingleValueEvent(valueEventListener);
    }
}