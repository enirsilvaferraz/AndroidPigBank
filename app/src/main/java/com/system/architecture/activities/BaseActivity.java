package com.system.architecture.activities;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by eferraz on 05/12/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public void showMessage(Throwable e) {
        showMessage(e.getMessage(), Snackbar.LENGTH_INDEFINITE);
    }

    public View getContainer() {
        return getWindow().getDecorView().findViewById(android.R.id.content);
    }

    public void showMessage(int message) {
        showMessage(getString(message), Snackbar.LENGTH_LONG);
    }

    private void showMessage(String message, int timeMessage) {
        Snackbar.make(getContainer(), message, timeMessage)
                .setAction("Close", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }).show();
    }
}
