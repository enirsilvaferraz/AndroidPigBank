package com.system.androidpigbank.controllers;

import android.app.IntentService;
import android.content.Intent;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.annotations.Nullable;
import com.google.gson.Gson;
import com.system.androidpigbank.helpers.FirebaseInstance;
import com.system.androidpigbank.models.business.CategoryBusiness;
import com.system.androidpigbank.models.business.TransactionBusiness;
import com.system.androidpigbank.models.entities.EntityAbs;
import com.system.androidpigbank.models.persistences.DaoAbs;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BackupService extends IntentService {

    private List<ObserverImpl> listObserver;

    public BackupService() {
        super(BackupService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {

            listObserver = new ArrayList<>();

            listObserver.add(new ObserverImpl<>(FirebaseInstance.getInstance(this).getCategoryChild(), new CategoryBusiness(this)));
            listObserver.add(new ObserverImpl<>(FirebaseInstance.getInstance(this).getTransactionChild(), new TransactionBusiness(this)));

            executeNext(null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void executeNext(@Nullable ObserverImpl observer) {

        if (listObserver == null || listObserver.isEmpty()) {
            return;
        }

        int indexOf = 0;
        if (observer != null) {
            indexOf = listObserver.indexOf(observer) + 1;
        }

        if (listObserver.size() > indexOf) {
            listObserver.get(indexOf).onFinish();
        }
    }

    private class ObserverImpl<T extends EntityAbs> {

        final private Firebase firebase;
        final private DaoAbs business;

        public ObserverImpl(Firebase firebase, DaoAbs business) {
            this.firebase = firebase;
            this.business = business;
        }

        public void onFinish() {

            try {

                final List<EntityAbs> entityList = business.findAll();
                final List<EntityAbs> removeList = new ArrayList<>();

                final Map<String, Object> map = new HashMap<>();
                for (EntityAbs entityAbs : entityList) {

                    if (entityAbs.isActive()) {
                        map.put(entityAbs.getId().toString(), new Gson().toJson(entityAbs));
                    } else {
                        removeList.add(entityAbs);
                    }
                }

                firebase.updateChildren(map, new Firebase.CompletionListener() {

                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {

                        try {

                            final Class<?> classOfT = Class.forName(firebase.getKey().replace("_", "."));
                            for (Object entityString : map.values()) {
                                EntityAbs entity = (EntityAbs) new Gson().fromJson(entityString.toString(), classOfT);
                                entity.setAlreadySync(true);
                                business.edit(entity);
                            }

                            for (EntityAbs entity : removeList) {
                                firebase.child(entity.getId().toString()).setValue(null);
                                business.deleteLogic(entity);
                            }

                        } catch (SQLException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                        executeNext(ObserverImpl.this);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}