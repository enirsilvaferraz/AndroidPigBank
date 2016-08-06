package com.system.androidpigbank.architecture.utils;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by eferraz on 03/08/16.
 * Classes de utilitarios
 */

public final class JavaUtils {

    /**
     *
     */
    public static class DateUtil {

        public static final String MMMM_YYYY = "MMMM / yyyy";
        private static final String DD_MM_YYYY = "dd/MM/yyyy";

        public static String format(Date date, String template) {
            final String format = new SimpleDateFormat(template, new Locale("pt", "BR")).format(date);
            return format.substring(0, 1).toUpperCase() + format.substring(1);
        }

        public static String format(Date date) {
            return format(date, DD_MM_YYYY);
        }

        public static Date parse(String date) throws ParseException {
            return parse(date, DD_MM_YYYY);
        }

        private static Date parse(String date, String template) throws ParseException {
            return new SimpleDateFormat(template, new Locale("pt", "BR")).parse(date);
        }
    }

    /**
     *
     */
    public static class NumberUtil {

        public static String currencyFormat(String value) {
            return currencyFormat(Double.valueOf(value));
        }

        public static String currencyFormat(Double value) {
            final NumberFormat instance = NumberFormat.getInstance(new Locale("pt", "BR"));
            instance.setMinimumFractionDigits(2);
            instance.setMinimumIntegerDigits(1);
            return "R$ " + instance.format(value);
        }
    }
}
