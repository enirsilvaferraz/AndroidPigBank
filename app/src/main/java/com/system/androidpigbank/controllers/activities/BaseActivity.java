package com.system.androidpigbank.controllers.activities;

import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.system.androidpigbank.controllers.managers.LoaderResult;

/**
 * Created by eferraz on 05/12/15.
 */
public abstract class BaseActivity<T> extends AppCompatActivity {

    public void showMessage(Throwable e) {
        showMessage(e.getMessage());
    }

    public void showMessage(String message) {
        Snackbar.make(getContainer(), message, Snackbar.LENGTH_LONG).show();
    }

    protected abstract View getContainer();
}
