package com.system.androidpigbank.helpers;

import android.content.Context;
import android.content.Intent;

import com.system.androidpigbank.models.business.BackupService;

/**
 * Created by eferraz on 25/04/16.
 */
public final class IntentRouter {

    public static void startServiceBackup(Context context){
        context.startService(new Intent(context, BackupService.class));
    }
}
