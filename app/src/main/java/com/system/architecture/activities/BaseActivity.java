package com.system.architecture.activities;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by eferraz on 05/12/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public void showMessage(Throwable e) {
        showMessage(e.getMessage());
    }

    public View getContainer() {
        return getWindow().getDecorView().findViewById(android.R.id.content);
    }

    public void showMessage(int message) {
        showMessage(getString(message), Snackbar.LENGTH_LONG);
    }

    public void showMessage(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
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
