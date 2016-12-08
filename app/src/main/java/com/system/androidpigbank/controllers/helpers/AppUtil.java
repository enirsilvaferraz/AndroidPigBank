package com.system.androidpigbank.controllers.helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.system.architecture.utils.JavaUtils;

import java.util.Calendar;
import java.util.Date;

public class AppUtil {

    public static Quinzena getQuinzena(Date date) {
        return JavaUtils.DateUtil.get(Calendar.DATE, date) < 20 ? Quinzena.PRIMEIRA : Quinzena.SEGUNDA;
    }

    public static void showMessage(String message, Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
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
}