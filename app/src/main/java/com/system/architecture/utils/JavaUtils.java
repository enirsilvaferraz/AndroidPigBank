package com.system.architecture.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.TypedValue;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.system.androidpigbank.BuildConfig;
import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.activities.HomeActivity;
import com.system.androidpigbank.models.firebase.serializers.GsonCategorySerializer;
import com.system.androidpigbank.models.firebase.serializers.GsonDateSerializer;
import com.system.androidpigbank.models.firebase.serializers.GsonPaymentTypeSerializer;
import com.system.androidpigbank.models.firebase.dtos.DTOAbs;
import com.system.androidpigbank.models.sqlite.entities.Category;
import com.system.androidpigbank.models.sqlite.entities.EntityAbs;
import com.system.androidpigbank.models.sqlite.entities.PaymentType;

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
                Crashlytics.logException(e);
                throw new RuntimeException(e);
            }
        }

        public static Date getActualMaximum(int year, int month){
            Calendar cInit = Calendar.getInstance();
            cInit.set(year, month, 1, cInit.getActualMaximum(Calendar.HOUR_OF_DAY), cInit.getActualMaximum(Calendar.MINUTE),cInit.getActualMaximum(Calendar.SECOND) ); // DATE = 1 Evita avancar o mes (31)
            cInit.set(Calendar.DATE, cInit.getActualMaximum(Calendar.DATE));
            return cInit.getTime();
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

        private static final String FLAVOR_PRD = "prd";

        public static void installShortCut(Context appContext){

            Intent shortcutIntent = new Intent(appContext, HomeActivity.class);
            shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            Intent addIntent = new Intent();
            addIntent.putExtra("duplicate", false);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, appContext.getString(R.string.app_name));
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(appContext, R.mipmap.ic_launcher));
            addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            appContext.sendBroadcast(addIntent);
        }

        public static boolean isProd() {
            return BuildConfig.FLAVOR.equals(FLAVOR_PRD);
        }

        public static int getPixel(Resources resources, int dp){
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        }
    }

    /**
     *
     */
    public static class GsonUtil {

        private Gson build;

        public static GsonUtil getInstance(){
            return new GsonUtil();
        }

        public GsonUtil fromTransaction(){
            build = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .registerTypeAdapter(Date.class, new GsonDateSerializer())
                    .registerTypeAdapter(PaymentType.class, new GsonPaymentTypeSerializer())
                    .registerTypeAdapter(Category.class, new GsonCategorySerializer())
                    .create();
            return this;
        }

        public GsonUtil fromCategory (){
            build =  new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();
            return this;
        }

        public DTOAbs toDTO(EntityAbs entity, Class<? extends DTOAbs> classe){
            String json = build.toJson(entity);
            return build.fromJson(json, classe);
        }


        public EntityAbs toEntity(DTOAbs dto, Class<? extends EntityAbs> classe) {
            String json = build.toJson(dto);
            return build.fromJson(json, classe);
        }
    }
}
