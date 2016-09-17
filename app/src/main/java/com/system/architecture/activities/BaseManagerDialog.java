package com.system.architecture.activities;

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
        getFirebaseBusinessInstance().save(model);
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        ((HomeActivity) activity).callApi();
        dismiss();

//        final AppCompatActivity activity = (AppCompatActivity) getActivity();
//        ManagerHelper.execute(activity, new ManagerHelper.LoaderResultInterface<T>() {
//
//            @Override
//            public T executeAction() throws Exception {
//                        prepareToPersist();
//                        return getBusinessInstance().save(model);
//            }
//
//            @Override
//            public int loaderId() {
//                return Constants.LOADER_DEFAULT_ID;
//            }
//
//            @Override
//            public void onComplete(LoaderResult<T> data) {
//                if (data.isSuccess()) {
//                    IntentRouter.startServiceBackup(activity);
//                    ((HomeActivity) activity).callApi();
//
//                    Handler handler = new Handler(){
//                        @Override
//                        public void handleMessage(Message msg) {
//                            dismiss();
//                        }
//                    };
//                    handler.sendEmptyMessage(0);
//
//                } else {
//                    ((BaseActivity) activity).showMessage(data.getException());
//                }
//            }
//        });
    }

    protected abstract FirebaseDaoAbs<T> getFirebaseBusinessInstance();

    protected abstract void prepareToPersist() throws Exception;
}
