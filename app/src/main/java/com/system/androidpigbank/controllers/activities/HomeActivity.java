package com.system.androidpigbank.controllers.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.adapters.pager.SectionsCurrentMonthPagerAdapter;
import com.system.androidpigbank.controllers.adapters.recyclerv.MonthAdapter;
import com.system.androidpigbank.controllers.fragments.MonthFragment;
import com.system.androidpigbank.controllers.helpers.IntentRouter;
import com.system.androidpigbank.controllers.helpers.constant.Constants;
import com.system.androidpigbank.controllers.vos.HomeObject;
import com.system.androidpigbank.models.sqlite.business.CategoryBusiness;
import com.system.androidpigbank.models.sqlite.business.RecoverBusiness;
import com.system.androidpigbank.models.sqlite.business.TransactionBusiness;
import com.system.architecture.activities.BaseNavigationDrawerActivity;
import com.system.architecture.adapters.CardFragment;
import com.system.architecture.helpers.PermissionHelper;
import com.system.architecture.managers.LoaderResult;
import com.system.architecture.managers.ManagerHelper;
import com.system.architecture.utils.JavaUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class HomeActivity extends BaseNavigationDrawerActivity {

    private static final List<String> ACCESS_PERMISSIONS = Arrays.asList(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    private static final int HOME_INDICATOR = 1;

    @BindView(R.id.container)
    ViewPager mViewPager;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private HomeObject data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_home_drawer);
        ButterKnife.bind(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentRouter.startTransactionManager(HomeActivity.this, null);
            }
        });

        if (savedInstanceState == null) {
            Calendar calendar = Calendar.getInstance();
            update(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
        } else {
            data = savedInstanceState.getParcelable(HomeObject.class.getSimpleName());
            if (data == null) {
                data = new HomeObject();
            }
            configureResult(data);
        }

        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof CardFragment) {
            switch (((CardFragment) fragment).getFragmentID()) {
                case Constants.FRAGMENT_ID_SUMMARY_CATEGORY:
                    ((CardFragment) fragment).setData(new CategoryBusiness(this).organizeCategorySummaryList(data.getListCategorySummary()));
                    break;

                case Constants.FRAGMENT_ID_TRANSACTION:
                    ((CardFragment) fragment).setData(new TransactionBusiness(this).organizeTransationcList(data.getListTransaction()));
                    break;
            }
        } else if (fragment instanceof MonthFragment) {
            ((MonthFragment) fragment).setData(data.getListMonth());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(HomeObject.class.getSimpleName(), data);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {

        PermissionHelper.verifyPermissionAlert(this, permissions, grantResults, new PermissionHelper.PermissionCallBack() {

            @Override
            public void executeAction(String permission) {
                try {
                    if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(permission)) {
                        Calendar calendar = Calendar.getInstance();
                        update(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
                    }
                } catch (Exception e) {
                    showMessage(e);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (RESULT_OK == resultCode && (Constants.REQUEST_ACTION_DELETE == requestCode || Constants.REQUEST_ACTION_SAVE == requestCode)) {
            callApi(this.data.getMonth(), this.data.getYear());
            showMessage(data.getIntExtra(Constants.BUNDLE_MESSAGE_ID, 0));
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void callApi() {
        callApi(this.data.getMonth(), this.data.getYear());
    }

    private void update(final int month, final int year) {
        if (PermissionHelper.checkForPermissions(this, ACCESS_PERMISSIONS)) {
            callApi(month, year);
        } else {
            PermissionHelper.requestPermissions(this, ACCESS_PERMISSIONS, Constants.REQUEST_PERMISSION_DEFAULT_ID);
        }
    }

    private void callApi(final int month, final int year) {

        ManagerHelper.execute(this, new ManagerHelper.LoaderResultInterface<HomeObject>() {

            private ProgressDialog dialog;

            @Override
            public void onPreLoad() {
                dialog = ProgressDialog.show(HomeActivity.this, "", "Loading. Please wait...", true);
            }

            @Override
            public HomeObject executeAction() throws Exception {

                SharedPreferences sp = getSharedPreferences("SHARED_APP", Context.MODE_PRIVATE);
                if (!sp.getBoolean("FIST_ACCESS", false)) {

                    RecoverBusiness.getInstance().execute(HomeActivity.this);

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("FIST_ACCESS", true);
                    editor.apply();
                }

                HomeObject object = new HomeObject();
                object.setMonth(month);
                object.setYear(year);
                object.setListCategorySummary(new CategoryBusiness(HomeActivity.this).getSummaryCategoryByMonth(month, year));
                object.setListTransaction(new TransactionBusiness(HomeActivity.this).getTransactionByMonth(month, year));
                object.setListMonth(new TransactionBusiness(HomeActivity.this).getMonthWithTransaction(year));
                return object;
            }

            @Override
            public void onComplete(LoaderResult<HomeObject> data) {
                if (data.isSuccess()) {
                    configureResult(data.getData());
                } else {
                    showMessage(data.getException());
                }

                dialog.dismiss();
            }
        });
    }

    private void configureResult(HomeObject data) {
        this.data = data;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, data.getYear());
        calendar.set(Calendar.MONTH, data.getMonth());
        setTitle(JavaUtils.DateUtil.format(calendar.getTime(), JavaUtils.DateUtil.MMMM_DE_YYYY));

        SectionsCurrentMonthPagerAdapter adapter = new SectionsCurrentMonthPagerAdapter(getSupportFragmentManager());
        adapter.setOnItemClicked(new OnItemClickedListener());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(HOME_INDICATOR);
        mViewPager.getAdapter().notifyDataSetChanged();
    }

    private class OnItemClickedListener implements MonthAdapter.OnItemClicked {

        @Override
        public void onClick(Date date) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            setTitle(JavaUtils.DateUtil.format(calendar.getTime(), JavaUtils.DateUtil.MMMM_DE_YYYY));
            update(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
        }
    }
}
