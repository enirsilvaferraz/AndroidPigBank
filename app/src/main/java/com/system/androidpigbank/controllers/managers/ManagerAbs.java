package com.system.androidpigbank.controllers.managers;

import android.content.Context;

import com.system.androidpigbank.models.entities.Transaction;

/**
 * Created by eferraz on 05/12/15.
 */
public abstract class ManagerAbs {

    private final Context context;

    public ManagerAbs(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

}
