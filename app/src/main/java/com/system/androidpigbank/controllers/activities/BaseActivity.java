package com.system.androidpigbank.controllers.activities;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by eferraz on 05/12/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public void showMessage(Throwable e) {
        Snackbar.make(getContainer(), e.getMessage(), Snackbar.LENGTH_LONG).show();
    }

    public void showMessage(int message) {
        Snackbar.make(getContainer(), message, Snackbar.LENGTH_LONG).show();
    }

    protected abstract View getContainer();
}
