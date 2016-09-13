package com.system.androidpigbank.controllers.helpers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.system.androidpigbank.controllers.activities.CategoryListActivity;
import com.system.androidpigbank.controllers.activities.CategoryManagerDialog;
import com.system.androidpigbank.controllers.activities.TransactionManagerDialog;
import com.system.androidpigbank.controllers.helpers.constant.Constants;
import com.system.androidpigbank.models.sqlite.business.BackupService;
import com.system.androidpigbank.models.sqlite.entities.Category;
import com.system.androidpigbank.models.sqlite.entities.Transaction;

/**
 * Created by eferraz on 25/04/16.
 */
public final class IntentRouter {

    private static DialogFragment dialog;

    public static void startServiceBackup(Context context) {
        context.startService(new Intent(context, BackupService.class));
    }

    public static void startTransactionManager(AppCompatActivity context, Transaction model) {
        hideDialog();
        FragmentManager fm = context.getSupportFragmentManager();
        dialog = TransactionManagerDialog.newInstance(model);
        dialog.show(fm, TransactionManagerDialog.class.getSimpleName());
    }

    public static void startCategoryManager(AppCompatActivity context, Category model) {
        hideDialog();
        FragmentManager fm = context.getSupportFragmentManager();
        dialog = CategoryManagerDialog.newInstance(model);
        dialog.show(fm, CategoryManagerDialog.class.getSimpleName());
    }

    public static void hideDialog() {
        if (dialog!= null && dialog.isVisible()) {
            dialog.dismiss();
        }
    }

    public static void startCategorySummaryDetail(Context context, Category model) {
        Intent intent = new Intent(context, CategoryManagerDialog.class);
        intent.putExtra(Constants.BUNDLE_MODEL_DEFAULT, model);
        context.startActivity(intent);
    }

    public static void startCategoryList(Context context) {
        context.startActivity(new Intent(context, CategoryListActivity.class));
    }
}
