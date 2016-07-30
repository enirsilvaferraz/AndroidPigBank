package com.system.androidpigbank.architecture.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;

import com.system.androidpigbank.R;
import com.system.androidpigbank.architecture.managers.LoaderResult;
import com.system.androidpigbank.architecture.managers.ManagerHelper;
import com.system.androidpigbank.helpers.IntentRouter;
import com.system.androidpigbank.architecture.helpers.PermissionHelper;
import com.system.androidpigbank.helpers.constant.Constants;
import com.system.androidpigbank.models.entities.EntityAbs;
import com.system.androidpigbank.models.persistences.DaoAbs;

import java.util.Arrays;
import java.util.List;

/**
 * Created by eferraz on 25/04/16.
 * Classe que abstrai as principais funcoes das activities de manager
 */
public abstract class BaseManagerActivity<T extends EntityAbs> extends BaseActivity {

    private static final List<String> ACCESS_PERMISSIONS = Arrays.asList(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE});
    protected T model;
    private Action action;

    protected void execute(Action action) throws Exception {

        this.action = action;

        if (PermissionHelper.checkForPermissions(this, ACCESS_PERMISSIONS)) {

            switch (action) {
                case SAVE:
                    save();
                    break;

                case DELETE:
                    delete();
                    break;
            }

        } else {
            PermissionHelper.requestPermissions(this, ACCESS_PERMISSIONS, Constants.REQUEST_PERMISSION_DEFAULT_ID);
        }
    }

    private void delete() {

        ManagerHelper.execute(this, new ManagerHelper.LoaderResultInterface<T>() {

            @Override
            public T executeAction() throws Exception {
                return getBusinessInstance().delete(model);
            }

            @Override
            public int loaderId() {
                return Constants.LOADER_DEFAULT_ID;
            }

            @Override
            public void onComplete(LoaderResult<T> data) {
                if (data.isSuccess()) {
                    showMessage(R.string.message_saved_sucess);
                    IntentRouter.startServiceBackup(BaseManagerActivity.this);
                    BaseManagerActivity.this.finish();
                } else {
                    showMessage(data.getException());
                }
            }
        });
    }

    private void save() throws Exception {

        prepareToPersist();

        ManagerHelper.execute(this, new ManagerHelper.LoaderResultInterface<T>() {

            @Override
            public T executeAction() throws Exception {
                return getBusinessInstance().save(model);
            }

            @Override
            public int loaderId() {
                return Constants.LOADER_DEFAULT_ID;
            }

            @Override
            public void onComplete(LoaderResult<T> data) {
                if (data.isSuccess()) {
                    showMessage(R.string.message_saved_sucess);
                    IntentRouter.startServiceBackup(BaseManagerActivity.this);
                    BaseManagerActivity.this.finish();
                } else {
                    showMessage(data.getException());
                }
            }
        });
    }

    protected abstract DaoAbs<T> getBusinessInstance();

    protected abstract void prepareToPersist() throws Exception;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_base_manager, menu);
        if (model == null || model.getId() == null) {
            menu.getItem(1).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        try {

            int id = item.getItemId();
            if (id == R.id.base_manager_act_save) {
                execute(Action.SAVE);
                return true;
            } else if (id == R.id.base_manager_act_delete) {
                execute(Action.DELETE);
                return true;
            }

        } catch (Exception e) {
            showMessage(e);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {

        PermissionHelper.verifyPermissionAlert(this, permissions, grantResults, new PermissionHelper.PermissionCallBack() {

            @Override
            public void onSuccess(String permission) {
                try {
                    execute(action);
                } catch (Exception e) {
                    showMessage(e);
                }
            }

            @Override
            public void onError(String permission) {

                new AlertDialog.Builder(BaseManagerActivity.this)
                        .setMessage(BaseManagerActivity.this.getString(R.string.permission_required_message))
                        .setCancelable(false)
                        .setPositiveButton(R.string.system_ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                PermissionHelper.callAppSettings(BaseManagerActivity.this);
                            }
                        })
                        .setNegativeButton(R.string.system_cancel, null)
                        .create()
                        .show();
            }
        });
    }

    public enum Action {
        SAVE, DELETE
    }

}
