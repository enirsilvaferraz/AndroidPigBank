package com.system.androidpigbank.controllers.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.system.androidpigbank.R;
import com.system.androidpigbank.architecture.activities.BaseManagerActivity;
import com.system.androidpigbank.architecture.activities.BaseNavigationDrawerActivity;
import com.system.androidpigbank.controllers.adapters.pager.SectionsCurrentMonthPagerAdapter;
import com.system.androidpigbank.controllers.adapters.recyclerv.MonthAdapter;
import com.system.androidpigbank.helpers.PermissionHelper;
import com.system.androidpigbank.helpers.constant.Constants;
import com.system.androidpigbank.models.business.RecoverService;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategorySummaryActivity extends BaseNavigationDrawerActivity {

    private static final List<String> ACCESS_PERMISSIONS = Arrays.asList(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE});
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

        SectionsCurrentMonthPagerAdapter adapter = new SectionsCurrentMonthPagerAdapter(getSupportFragmentManager());
        adapter.setOnItemClicked(new MonthAdapter.OnItemClicked() {
            @Override
            public void onClick(Date date) {

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                final String title = new SimpleDateFormat("MMMM 'de' yyyy").format(calendar.getTime());
                setTitle(title.substring(0, 1).toUpperCase() + title.substring(1));

                ((SectionsCurrentMonthPagerAdapter)mViewPager.getAdapter())
                        .setCurrentTime(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));

                mViewPager.setCurrentItem(HOME_INDICATOR);
            }
        });

        mViewPager.setAdapter(adapter);
        //mViewPager.setOffscreenPageLimit(2);
        mViewPager.setCurrentItem(HOME_INDICATOR);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategorySummaryActivity.this, TransactionManagerActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_transaction_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.transaction_history_act_recover) {

            if (PermissionHelper.checkForPermissions(this, ACCESS_PERMISSIONS)) {
                startService(new Intent(this, RecoverService.class));
                return true;
            } else {
                PermissionHelper.requestPermissions(this, ACCESS_PERMISSIONS, Constants.REQUEST_PERMISSION_DEFAULT_ID);
                return false;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {

        PermissionHelper.verifyPermissionAlert(this, permissions, grantResults, new PermissionHelper.PermissionCallBack() {

            @Override
            public void onSuccess(String permission) {
                try {
                    startService(new Intent(getBaseContext(), RecoverService.class));
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
                                PermissionHelper.callAppSettings(CategorySummaryActivity.this);
                            }
                        })
                        .setNegativeButton(R.string.system_cancel, null)
                        .create()
                        .show();
            }
        });
    }

    @Override
    public View getContainer() {
        return mViewPager;
    }

}
