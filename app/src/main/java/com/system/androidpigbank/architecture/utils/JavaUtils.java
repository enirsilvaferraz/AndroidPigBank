package com.system.androidpigbank.architecture.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by eferraz on 03/08/16.
 */

public final class JavaUtils {

    /**
     *
     */
    public static class DateUtil {

        private static final String DD_MM_YYYY = "dd/MM/yyyy";

        public static String format (Date date, String template){
            return new SimpleDateFormat(template, new Locale("pt", "BR")).format(date);
        }

        public static String format (Date date){
            return format(date, DD_MM_YYYY);
        }

        public static Date parse(String date) throws ParseException {
            return parse(date, DD_MM_YYYY);
        }

        private static Date parse(String date, String template) throws ParseException {
            return new SimpleDateFormat(template, new Locale("pt", "BR")).parse(date);
        }
    }
}
