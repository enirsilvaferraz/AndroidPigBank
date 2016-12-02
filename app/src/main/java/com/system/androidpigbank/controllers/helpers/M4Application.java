package com.system.androidpigbank.controllers.helpers;

import android.app.Application;

import com.system.architecture.models.FirebaseAbs;

/**
 * Created by eferraz on 01/12/16.
 */

public class M4Application extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //FirebaseAbs.enableOffline();
    }
}
