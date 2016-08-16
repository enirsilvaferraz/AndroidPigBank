package com.system.androidpigbank.controllers.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.system.androidpigbank.R;
import com.system.androidpigbank.architecture.activities.BaseNavigationDrawerActivity;
import com.system.androidpigbank.architecture.utils.JavaUtils;
import com.system.androidpigbank.controllers.adapters.pager.SectionsCurrentMonthPagerAdapter;
import com.system.androidpigbank.controllers.adapters.recyclerv.MonthAdapter;
import com.system.androidpigbank.architecture.managers.LoaderResult;
import com.system.androidpigbank.architecture.managers.ManagerHelper;
import com.system.androidpigbank.controllers.vos.HomeObject;
import com.system.androidpigbank.controllers.helpers.IntentRouter;
import com.system.androidpigbank.architecture.helpers.PermissionHelper;
import com.system.androidpigbank.controllers.helpers.constant.Constants;
import com.system.androidpigbank.models.business.CategoryBusiness;
import com.system.androidpigbank.models.business.RecoverBusiness;
import com.system.androidpigbank.models.business.TransactionBusiness;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseNavigationDrawerActivity {

    private static final List<String> ACCESS_PERMISSIONS = Arrays.asList(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    private static final int HOME_INDICATOR = 1;

    @BindView(R.id.container)
    ViewPager mViewPager;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_category_summary_drawer);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);

        final String title = new SimpleDateFormat("MMMM 'de' yyyy").format(Calendar.getInstance().getTime());
        setTitle(title.substring(0, 1).toUpperCase() + title.substring(1));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentRouter.startTransactionManager(HomeActivity.this);
            }
        });

        SectionsCurrentMonthPagerAdapter adapter = new SectionsCurrentMonthPagerAdapter(getSupportFragmentManager());
        adapter.setOnItemClicked(new OnItemClickedListener());

        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(3);

        Calendar calendar = Calendar.getInstance();
        update(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {

        PermissionHelper.verifyPermissionAlert(this, permissions, grantResults, new PermissionHelper.PermissionCallBack() {

            @Override
            public void onSuccess(String permission) {
                try {

                    Calendar calendar = Calendar.getInstance();
                    update(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));

                } catch (Exception e) {
                    showMessage(e);
                }
            }

            @Override
            public void onError(String permission) {

                new AlertDialog.Builder(getBaseContext())
                        .setMessage(getBaseContext().getString(R.string.permission_required_message))
                        .setCancelable(false)
                        .setPositiveButton(R.string.system_ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                PermissionHelper.callAppSettings(HomeActivity.this);
                            }
                        })
                        .setNegativeButton(R.string.system_cancel, null)
                        .create()
                        .show();
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

        final ProgressDialog dialog = ProgressDialog.show(HomeActivity.this, "", "Loading. Please wait...", true);

        ManagerHelper.execute(this, new ManagerHelper.LoaderResultInterface<HomeObject>() {

            @Override
            public HomeObject executeAction() throws Exception {

                dialog.show();

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
                    mViewPager.getAdapter().notifyDataSetChanged();
                    mViewPager.setCurrentItem(HOME_INDICATOR);
                    ((SectionsCurrentMonthPagerAdapter)mViewPager.getAdapter()).update(data.getData());
                } else {
                    showMessage(data.getException());
                }

                dialog.hide();
            }
        });
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
