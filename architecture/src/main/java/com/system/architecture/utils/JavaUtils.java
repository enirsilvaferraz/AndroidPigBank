package com.system.architecture.utils;

import android.content.res.Resources;
import android.util.TypedValue;

import java.lang.reflect.ParameterizedType;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

        public static final String DD_MM_YYYY = "dd/MM/yyyy";
        public static final String MMMM_DE_YYYY = "MMMM 'de' yyyy";
        public static final String YYYY_MM_DD = "yyyy/MM/dd";
        public static final String MM_YYYY = "MM/yyyy";

        public static String format(Date date, String template) {
            if (date == null) {
                return "Not defined";
            }
            final String format = new SimpleDateFormat(template, new Locale("pt", "BR")).format(date);
            return format.substring(0, 1).toUpperCase() + format.substring(1);
        }

        public static String format(Date date) {
            return format(date, DD_MM_YYYY);
        }

        public static Date parse(String date) throws ParseException {
            return parse(date, DD_MM_YYYY);
        }

        public static Date parse(String date, String template) {
            try {
                return new SimpleDateFormat(template, new Locale("pt", "BR")).parse(date);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        public static Date getActualMaximum(int year, int month) {
            Calendar cInit = Calendar.getInstance();
            cInit.set(year, month, 1); // DATE = 1 Evita avancar o mes (31)
            cInit.set(Calendar.DATE, cInit.getActualMaximum(Calendar.DATE));
            cInit.set(Calendar.HOUR_OF_DAY, cInit.getActualMaximum(Calendar.HOUR_OF_DAY));
            cInit.set(Calendar.MINUTE, cInit.getActualMaximum(Calendar.MINUTE));
            cInit.set(Calendar.SECOND, cInit.getActualMaximum(Calendar.SECOND));
            return cInit.getTime();
        }

        public static Date getActualMinimum(int year, int month) {
            Calendar cInit = Calendar.getInstance();
            cInit.set(year, month, cInit.getActualMinimum(Calendar.DATE));
            cInit.set(Calendar.HOUR_OF_DAY, cInit.getActualMinimum(Calendar.HOUR_OF_DAY));
            cInit.set(Calendar.MINUTE, cInit.getActualMinimum(Calendar.MINUTE));
            cInit.set(Calendar.SECOND, cInit.getActualMinimum(Calendar.SECOND));
            return cInit.getTime();
        }

        public static int compare(Date dInit, Date dEnd) {

            Calendar cInit = Calendar.getInstance();
            cInit.setTime(parse(format(dInit, DD_MM_YYYY), DD_MM_YYYY));

            Calendar cEnd = Calendar.getInstance();
            cEnd.setTime(parse(format(dEnd, DD_MM_YYYY), DD_MM_YYYY));

            return cInit.compareTo(cEnd);
        }

        public static int get(int constantCalendar, Date date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(constantCalendar);
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

    /**
     *
     */
    public static class StringUtil {

        public static boolean isEmpty(String string) {
            return string == null || string.trim().isEmpty();
        }
    }

    /**
     *
     */
    public static class AndroidUtil {

        public static int getPixel(Resources resources, int dp) {
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        }
    }


    public static class ClassUtil {

        public static Class getTClass(Object object) {
            final ParameterizedType type = (ParameterizedType) object.getClass().getGenericSuperclass();
            return (Class) (type).getActualTypeArguments()[0];
        }
    }
}
