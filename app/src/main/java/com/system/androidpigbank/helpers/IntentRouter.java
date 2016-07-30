package com.system.androidpigbank.helpers;

import android.content.Context;
import android.content.Intent;

import com.system.androidpigbank.controllers.activities.TransactionManagerActivity;
import com.system.androidpigbank.models.business.BackupService;

/**
 * Created by eferraz on 25/04/16.
 */
public final class IntentRouter {

    public static void startServiceBackup(Context context) {
        context.startService(new Intent(context, BackupService.class));
    }

    public static void startTransactionManager(Context context) {
        context.startActivity(new Intent(context, TransactionManagerActivity.class));
    }
}
