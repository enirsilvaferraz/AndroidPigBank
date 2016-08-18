package com.system.androidpigbank.controllers.helpers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.system.androidpigbank.controllers.activities.CategoryListActivity;
import com.system.androidpigbank.controllers.activities.CategoryManagerActivity;
import com.system.androidpigbank.controllers.activities.TransactionManagerActivity;
import com.system.androidpigbank.controllers.activities.TransactionManagerDialog;
import com.system.androidpigbank.controllers.helpers.constant.Constants;
import com.system.androidpigbank.models.business.BackupService;
import com.system.androidpigbank.models.entities.Category;
import com.system.androidpigbank.models.entities.Transaction;

/**
 * Created by eferraz on 25/04/16.
 */
public final class IntentRouter {

    public static void startServiceBackup(Context context) {
        context.startService(new Intent(context, BackupService.class));
    }

    public static void startTransactionManager(AppCompatActivity context, Transaction model) {
        final Intent intent = new Intent(context, TransactionManagerActivity.class);

        if (model != null) {
            intent.putExtra(Constants.BUNDLE_MODEL_DEFAULT, model);
        }

        //context.startActivityForResult(intent, Constants.REQUEST_ACTION_SAVE);

        FragmentManager fm = context.getSupportFragmentManager();
        TransactionManagerDialog dialog = TransactionManagerDialog.newInstance(model);
        dialog.show(fm, TransactionManagerDialog.class.getSimpleName());

    }

    public static void startCategoryManager(Context context, Category model) {
        Intent intent = new Intent(context, CategoryManagerActivity.class);
        intent.putExtra(Constants.BUNDLE_MODEL_DEFAULT, model);
        context.startActivity(intent);
    }

    public static void startCategoryList(Context context) {
        context.startActivity(new Intent(context, CategoryListActivity.class));
    }
}
