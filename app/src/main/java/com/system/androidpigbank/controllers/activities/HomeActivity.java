package com.system.androidpigbank.controllers.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.system.androidpigbank.R;
import com.system.architecture.activities.BaseNavigationDrawerActivity;
import com.system.architecture.helpers.PermissionHelper;
import com.system.architecture.managers.LoaderResult;
import com.system.architecture.managers.ManagerHelper;
import com.system.architecture.utils.JavaUtils;
import com.system.androidpigbank.controllers.adapters.pager.SectionsCurrentMonthPagerAdapter;
import com.system.androidpigbank.controllers.adapters.recyclerv.MonthAdapter;
import com.system.androidpigbank.controllers.fragments.CategorySummaryFragment;
import com.system.androidpigbank.controllers.fragments.MonthFragment;
import com.system.androidpigbank.controllers.fragments.TransactionListFragment;
import com.system.androidpigbank.controllers.helpers.IntentRouter;
import com.system.androidpigbank.controllers.helpers.constant.Constants;
import com.system.androidpigbank.controllers.vos.HomeObject;
import com.system.androidpigbank.models.business.CategoryBusiness;
import com.system.androidpigbank.models.business.RecoverBusiness;
import com.system.androidpigbank.models.business.TransactionBusiness;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseNavigationDrawerActivity {

    private static final List<String> ACCESS_PERMISSIONS = Collections.singletonList(Manifest.permission.READ_EXTERNAL_STORAGE);
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

        setTitle(JavaUtils.DateUtil.format(Calendar.getInstance().getTime(), JavaUtils.DateUtil.MMMM_DE_YYYY));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentRouter.startTransactionManager(HomeActivity.this);
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
            configureList(data);
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof CategorySummaryFragment) {
            ((CategorySummaryFragment) fragment).setData(data.getListCategorySummary());
        } else if (fragment instanceof TransactionListFragment) {
            ((TransactionListFragment) fragment).setData(data.getListTransaction());
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
                    Calendar calendar = Calendar.getInstance();
                    update(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
                } catch (Exception e) {
                    showMessage(e);
                }
            }
        });
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
                object.setListCategorySummary(new CategoryBusiness(HomeActivity.this).getSummaryCategoryByMonth(month, year));
                object.setListTransaction(new TransactionBusiness(HomeActivity.this).getTransactionByMonth(month, year));
                object.setListMonth(new TransactionBusiness(HomeActivity.this).getMonthWithTransaction(year));
                return object;
            }

            @Override
            public void onComplete(LoaderResult<HomeObject> data) {
                if (data.isSuccess()) {
                    configureList(data.getData());
                } else {
                    showMessage(data.getException());
                }

                dialog.dismiss();
            }
        });
    }

    private void configureList(HomeObject data) {
        this.data = data;

        SectionsCurrentMonthPagerAdapter adapter = new SectionsCurrentMonthPagerAdapter(getSupportFragmentManager());
        adapter.setOnItemClicked(new OnItemClickedListener());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(HOME_INDICATOR);
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
