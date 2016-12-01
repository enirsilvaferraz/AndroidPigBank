package com.system.androidpigbank.controllers.helpers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.system.androidpigbank.controllers.activities.CategoryManagerDialog;
import com.system.androidpigbank.controllers.activities.EstimateManagerDialog;
import com.system.androidpigbank.controllers.activities.TransactionManagerDialog;
import com.system.androidpigbank.controllers.vos.CategoryVO;
import com.system.androidpigbank.controllers.vos.EstimateVO;
import com.system.androidpigbank.controllers.vos.TransactionVO;

/**
 * Created by eferraz on 25/04/16.
 */
public final class IntentRouter {

    private static DialogFragment dialog;

    public static void startTransactionManager(AppCompatActivity context, TransactionVO model) {
        hideDialog();
        FragmentManager fm = context.getSupportFragmentManager();
        dialog = TransactionManagerDialog.newInstance(model);
        dialog.show(fm, TransactionManagerDialog.class.getSimpleName());
    }

    public static void startCategoryManager(AppCompatActivity context, CategoryVO model) {
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

    public static void startCategorySummaryDetail(Context context, CategoryVO model) {
        Intent intent = new Intent(context, CategoryManagerDialog.class);
        intent.putExtra(Constants.BUNDLE_MODEL_DEFAULT, model);
        context.startActivity(intent);
    }

    public static void startEstimateManager(AppCompatActivity context, EstimateVO model) {
        hideDialog();
        FragmentManager fm = context.getSupportFragmentManager();
        dialog = EstimateManagerDialog.newInstance(model);
        dialog.show(fm, EstimateManagerDialog.class.getSimpleName());
    }
}
