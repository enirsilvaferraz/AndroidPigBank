package com.system.androidpigbank.controllers.activities;

import android.view.Menu;
import android.view.MenuItem;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.managers.LoaderResult;
import com.system.androidpigbank.controllers.managers.ManagerHelper;
import com.system.androidpigbank.helpers.Constants;
import com.system.androidpigbank.helpers.IntentRouter;
import com.system.androidpigbank.models.entities.EntityAbs;
import com.system.androidpigbank.models.persistences.DaoAbs;

/**
 * Created by eferraz on 25/04/16.
 * Classe que abstrai as principais funcoes das activities de manager
 */
public abstract class BaseManagerActivity<T extends EntityAbs> extends BaseActivity {

    protected T model;

    protected void delete() {

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

    protected void save() throws Exception {

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
                save();
                return true;
            } else if (id == R.id.base_manager_act_delete) {
                delete();
                return true;
            }

        } catch (Exception e) {
            showMessage(e);
        }
        return super.onOptionsItemSelected(item);
    }

}
