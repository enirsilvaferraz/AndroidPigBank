package com.system.androidpigbank.models.firebase.business;


import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.system.androidpigbank.controllers.vos.CategoryVO;
import com.system.androidpigbank.controllers.vos.TitleVO;
import com.system.androidpigbank.controllers.vos.TotalVO;
import com.system.androidpigbank.controllers.vos.TransactionVO;
import com.system.androidpigbank.controllers.vos.WhiteSpaceVO;
import com.system.androidpigbank.models.firebase.dtos.DTOAbs;
import com.system.androidpigbank.models.firebase.dtos.TransactionDTO;
import com.system.architecture.adapters.CardAdapter;
import com.system.architecture.utils.JavaUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Enir on 08/09/2016.
 */

public class TransactionFirebaseBusiness extends FirebaseDaoAbs<TransactionVO> {

    public TransactionVO save(TransactionVO transaction) {

        if (transaction.getCategory().getKey() == null) {
            CategoryVO category = new CategoryFirebaseBusiness().save(transaction.getCategory());
            category.setPrimary(true);
            transaction.setCategory(category);
        }

        if (transaction.getCategorySecondary() != null && transaction.getCategorySecondary().getKey() == null) {
            CategoryVO category = new CategoryFirebaseBusiness().save(transaction.getCategorySecondary());
            transaction.setCategorySecondary(category);
        }

        return super.save(transaction);
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
                        List<TransactionVO> list = new ArrayList<>();
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

    public List<CardAdapter.CardModel> organizeTransationcList(List<TransactionVO> list) {

        List<CardAdapter.CardModel> itens = new ArrayList<>();

        Double valorAcumular = 0D;
        // Double valorDiario = 0D;
        boolean hasTitleFutureLanc = false;

        itens.add(new WhiteSpaceVO());

        for (int position = 0; position < list.size(); position++) {

            TransactionVO transactionAct = list.get(position);
            TransactionVO transactionProx = list.size() > position + 1 ? list.get(position + 1) : null;

            if (JavaUtils.DateUtil.compare(transactionAct.getDatePayment(), Calendar.getInstance().getTime()) > 0) {

                if (!hasTitleFutureLanc && transactionProx != null) {
                    itens.add(new TitleVO("Lançamentos Futuros"));
                    itens.add(new WhiteSpaceVO());
                    hasTitleFutureLanc = true;
                }
                valorAcumular += transactionAct.getValue();
            }

            itens.add(transactionAct);
            //valorDiario += transactionAct.getValue();

            if (transactionProx == null || JavaUtils.DateUtil.compare(transactionAct.getDatePayment(), transactionProx.getDatePayment()) != 0) {
                itens.add(new TotalVO(null, valorAcumular));
                itens.add(new WhiteSpaceVO());
                //valorDiario = 0D;
            }
        }

        return itens;
    }

    public void findTransactionByCategory(CategoryVO category, final FirebaseMultiReturnListener listener) {

        getDatabaseReference()
                .orderByChild("category").equalTo(category.getOld().getName())
                //.orderByChild("categorySecondary").equalTo(category.getName())
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<TransactionVO> list = new ArrayList<>();
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