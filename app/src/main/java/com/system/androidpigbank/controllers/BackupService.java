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
            listObserver.add(new ObserverImpl<>(FirebaseInstance.getInstance(this).getCategoryChild(), new CategoryBusiness(this).findAll()));
            listObserver.add(new ObserverImpl<>(FirebaseInstance.getInstance(this).getTransactionChild(), new TransactionBusiness(this).findAll()));

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
        final private List<T> entityList;

        public ObserverImpl(Firebase firebase, List<T> entityList) {
            this.firebase = firebase;
            this.entityList = entityList;
        }

        public void onFinish() {

            final Map<String, Object> map = new HashMap<>();
            for (EntityAbs entityAbs : entityList) {
                map.put(entityAbs.getId().toString(), new Gson().toJson(entityAbs));
            }

            firebase.updateChildren(map, new Firebase.CompletionListener() {

                @Override
                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                    executeNext(ObserverImpl.this);
                }
            });
        }
    }
}