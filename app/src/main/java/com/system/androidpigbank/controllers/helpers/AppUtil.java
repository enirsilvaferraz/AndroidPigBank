package com.system.androidpigbank.controllers.helpers;

import com.system.architecture.utils.JavaUtils;

import java.util.Calendar;
import java.util.Date;

public class AppUtil {

    public static Quinzena getQuinzena(Date date) {
        return JavaUtils.DateUtil.get(Calendar.DATE, date) < 20 ? Quinzena.PRIMEIRA : Quinzena.SEGUNDA;
    }
}