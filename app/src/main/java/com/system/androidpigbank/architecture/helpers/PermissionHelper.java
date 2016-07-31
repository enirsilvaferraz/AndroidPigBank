package com.system.androidpigbank.architecture.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.system.androidpigbank.R;
import com.system.androidpigbank.architecture.activities.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eferraz on 03/11/15.
 * Class responsible to request permissions on Android M
 */
public class PermissionHelper {

    public static void requestPermissions(final Activity activity, final List<String> permissions, final int requestCode) {
        requestPermissions(activity, permissions != null ? permissions.toArray(new String[permissions.size()]) : null, requestCode);
    }

    public static void requestPermissions(final Activity activity, final String[] permissions, final int requestCode) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            List<String> permissionsToRequest = new ArrayList<>();

            /**
             * Add not granted permissions to {@link permissionsToRequest}
             */
            for (String permission : permissions) {
                if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(permission);
                }
            }

            if (!permissionsToRequest.isEmpty()) {
                final String[] array = permissionsToRequest.toArray(new String[permissionsToRequest.size()]);
                activity.requestPermissions(array, requestCode);
            }
        }
    }

    public static void verifyPermissionAlert(final Activity activity, final List<String> permissions, int[] grantResults, PermissionCallBack permissionCallback) {
        verifyPermissionAlert(activity, permissions != null ? permissions.toArray(new String[permissions.size()]) : null, grantResults, permissionCallback);
    }

    public static void verifyPermissionAlert(final Activity activity, final String[] permissions, int[] grantResults, PermissionCallBack permissionCallback) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            // For all permissions
            for (int iterator = 0; iterator < permissions.length; iterator++) {
                // Has permission
                if (grantResults[iterator] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted
                    permissionCallback.onSuccess(permissions[iterator]);
                }
                // Permission denied
                else {
                    // Already denied permission at least one time
                    if (activity.shouldShowRequestPermissionRationale(permissions[iterator])) {
                        // Show fail message on screen
                        notifyUI(activity);
                    }

                    // User did not deny or asked to do not request permission anymore
                    else {
                        // Fail gracefully
                        permissionCallback.onError(permissions[iterator]);
                    }

                    // Fail just once
                    break;
                }
            }
        }
    }

    private static void notifyUI(final Activity activity) {

        View view = activity instanceof BaseActivity ? ((BaseActivity) activity).getContainer() : ((AppCompatActivity) activity).getSupportFragmentManager().getFragments().get(0).getView();
        if (view != null) {
            Snackbar.make(view, R.string.system_permission_required, Snackbar.LENGTH_SHORT).show();
        }
    }

    public static boolean checkForPermission(Context context, String permission) {
        return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permission != null) || context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkForPermissions(Context context, List<String> permissions) {
        return checkForPermissions(context, permissions != null ? permissions.toArray(new String[permissions.size()]) : null);
    }

    private static boolean checkForPermissions(Context context, String[] permissions) {

        if (permissions != null) {
            for (String permission : permissions) {
                if (!checkForPermission(context, permission)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Call app settings to user manually change permissions
     */
    public static void callAppSettings(Activity activity) {

        final int REQUEST_APP_SETTINGS = 168;

        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + activity.getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        activity.startActivityForResult(myAppSettings, REQUEST_APP_SETTINGS);
    }

    public static void showDialogMessage(Context context, DialogInterface.OnClickListener onClickListenerOk, DialogInterface.OnClickListener onClickListenerCancel) {

        new AlertDialog.Builder(context)
                .setMessage(context.getString(R.string.permission_required_message))
                .setCancelable(false)
                .setPositiveButton(R.string.system_ok, onClickListenerOk)
                .setNegativeButton(R.string.system_cancel, onClickListenerCancel)
                .create()
                .show();

    }

    public interface PermissionCallBack {

        void onSuccess(String permission);

        void onError(String permission);
    }
}