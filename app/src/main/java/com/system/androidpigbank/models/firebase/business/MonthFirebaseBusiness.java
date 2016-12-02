package com.system.androidpigbank.models.firebase.business;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.system.androidpigbank.BuildConfig;
import com.system.androidpigbank.controllers.vos.MonthVO;
import com.system.androidpigbank.controllers.vos.TransactionVO;
import com.system.architecture.models.FirebaseAbs;
import com.system.architecture.models.DTOAbs;
import com.system.androidpigbank.models.firebase.dtos.MonthDTO;
import com.system.architecture.utils.JavaUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Enir on 08/09/2016.
 * Business de Month
 */

class MonthFirebaseBusiness extends FirebaseAbs<MonthVO> {

    public MonthFirebaseBusiness() {
        super(BuildConfig.FLAVOR);
    }

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

    public void update(final TransactionVO transaction, final FirebaseSingleReturnListener<MonthVO> listener) {

        final int month = JavaUtils.DateUtil.get(Calendar.MONTH, transaction.getDatePayment());
        final int year = JavaUtils.DateUtil.get(Calendar.YEAR, transaction.getDatePayment());

        final int monthOld = !JavaUtils.StringUtil.isEmpty(transaction.getKey()) ?
                JavaUtils.DateUtil.get(Calendar.MONTH, ((TransactionVO) transaction.getOld()).getDatePayment()) : month;
        final int yearOld = !JavaUtils.StringUtil.isEmpty(transaction.getKey()) ?
                JavaUtils.DateUtil.get(Calendar.YEAR, ((TransactionVO) transaction.getOld()).getDatePayment()) : year;

        findByYear(month, year, new FirebaseSingleReturnListener<MonthVO>() {

            @Override
            public void onFind(final MonthVO modelAtual) {

                // Para transacoes no mesmo mes
                if (month == monthOld && year == yearOld) {

                    // Se nao existir um mes grava-se um
                    if (modelAtual == null) {
                        save(new MonthVO(month, year, transaction.getValue(), 0D), listener);
                    } else {

                        Double value;
                        if (!JavaUtils.StringUtil.isEmpty(transaction.getKey())) {
                            // Se for update retira o valor antigo e soma o valor novo
                            value = transaction.getValue() - ((TransactionVO) transaction.getOld()).getValue();
                        } else {
                            value = transaction.getValue();
                        }

                        modelAtual.setValue(modelAtual.getValue() + value);

                        save(modelAtual, listener);
                    }

                } else {

                    // Subtrai no mes antigo
                    findByYear(monthOld, yearOld, new FirebaseSingleReturnListener<MonthVO>() {

                        @Override
                        public void onFind(MonthVO modelAnterior) {

                            modelAnterior.setValue(modelAnterior.getValue() - ((TransactionVO) transaction.getOld()).getValue());

                            // Salva o anterior
                            save(modelAnterior, new FirebaseSingleReturnListener<MonthVO>() {

                                @Override
                                public void onFind(MonthVO modelAnterior) {

                                    // Se nao existir um mes grava-se um
                                    if (modelAtual == null) {
                                        save(new MonthVO(month, year, transaction.getValue(), 0D), listener);
                                    } else {
                                        // Salva o atual
                                        modelAtual.setValue(modelAtual.getValue() + transaction.getValue());
                                        save(modelAtual, listener);
                                    }
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
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });
    }

    public void findByYear(final int month, int year, final FirebaseSingleReturnListener<MonthVO> listener) {

        ValueEventListener valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                MonthVO tInstance = null;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (getTInstance(postSnapshot).getMonth() == month) {
                        tInstance = getTInstance(postSnapshot);
                        tInstance.setKey(postSnapshot.getKey());
                        break;
                    }
                }
                listener.onFind(tInstance);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        };

        getDatabaseReference().orderByChild("year").equalTo(year)
                .addListenerForSingleValueEvent(valueEventListener);
    }

    public void delete(final TransactionVO transaction, final FirebaseSingleReturnListener listener) {

        final int month = JavaUtils.DateUtil.get(Calendar.MONTH, transaction.getDatePayment());
        final int year = JavaUtils.DateUtil.get(Calendar.YEAR, transaction.getDatePayment());

        findByYear(month, year, new FirebaseSingleReturnListener<MonthVO>() {

            @Override
            public void onFind(MonthVO model) {
                model.setValue(model.getValue() - transaction.getValue());
                save(model, listener);
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });

    }
}