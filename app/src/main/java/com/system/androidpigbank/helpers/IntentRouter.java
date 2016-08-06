package com.system.androidpigbank.helpers;

import android.content.Context;
import android.content.Intent;

import com.system.androidpigbank.architecture.activities.BaseNavigationDrawerActivity;
import com.system.androidpigbank.controllers.activities.CategoryListActivity;
import com.system.androidpigbank.controllers.activities.CategoryManagerActivity;
import com.system.androidpigbank.controllers.activities.FixedTransactionManagerActivity;
import com.system.androidpigbank.controllers.activities.TransactionManagerActivity;
import com.system.androidpigbank.helpers.constant.Constants;
import com.system.androidpigbank.models.business.BackupService;
import com.system.androidpigbank.models.entities.Category;

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

    public static void startCategoryManager(Context context, Category model) {
        Intent intent = new Intent(context, CategoryManagerActivity.class);
        intent.putExtra(Constants.BUNDLE_MODEL_DEFAULT, model);
        context.startActivity(intent);
    }

    public static void startCategoryList(Context context) {
        context.startActivity(new Intent(context, CategoryListActivity.class));
    }

    public static void startFixedTransactionManager(Context context) {
        context.startActivity(new Intent(context, FixedTransactionManagerActivity.class));
    }
}
