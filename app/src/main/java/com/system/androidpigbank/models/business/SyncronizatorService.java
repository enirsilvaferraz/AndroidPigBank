package com.system.androidpigbank.models.business;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by eferraz on 13/04/16.
 */
public class SyncronizatorService extends IntentService {

    public SyncronizatorService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {


    }
}
