package com.system.architecture.adapters;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

import com.system.androidpigbank.controllers.activities.HomeActivity;
import com.system.androidpigbank.models.firebase.business.FirebaseDaoAbs;
import com.system.architecture.managers.EntityAbs;

/**
 * Created by eferraz on 25/04/16.
 * Classe que abstrai as principais funcoes das activities de manager
 */
public abstract class BaseManagerDialog<T extends EntityAbs> extends DialogFragment {

    protected T model;

    protected void save() throws Exception {
        prepareToPersist();
        getFirebaseBusinessInstance().save(model, new FirebaseDaoAbs.FirebaseSingleReturnListener<T>() {
            @Override
            public void onFind(T list) {
                final AppCompatActivity activity = (AppCompatActivity) getActivity();
                ((HomeActivity) activity).callApi();
                dismiss();
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    protected abstract FirebaseDaoAbs<T> getFirebaseBusinessInstance();

    protected abstract void prepareToPersist() throws Exception;
}
