package com.system.androidpigbank.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.system.androidpigbank.R;
import com.system.androidpigbank.architecture.activities.BaseNavigationDrawerActivity;
import com.system.androidpigbank.controllers.adapters.pager.SectionsMonthPagerAdapter;
import com.system.androidpigbank.models.business.RecoverService;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionHistoryActivity extends BaseNavigationDrawerActivity {

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.container)
    ViewPager mViewPager;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_transaction_history_drawer);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);

        mViewPager.setAdapter(new SectionsMonthPagerAdapter(getSupportFragmentManager()));
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setCurrentItem(Calendar.getInstance().get(Calendar.MONTH));

        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TransactionHistoryActivity.this, TransactionManagerActivity.class));
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
            startService(new Intent(this, RecoverService.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View getContainer() {
        return mViewPager;
    }
}
