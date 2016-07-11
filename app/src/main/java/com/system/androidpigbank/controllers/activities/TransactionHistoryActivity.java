package com.system.androidpigbank.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.system.androidpigbank.R;
import com.system.androidpigbank.architecture.activities.BaseNavigationDrawerActivity;
import com.system.androidpigbank.controllers.adapters.pager.SectionsPagerAdapter;
import com.system.androidpigbank.models.business.RecoverService;

import java.util.Calendar;

public class TransactionHistoryActivity extends BaseNavigationDrawerActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_transaction_history_drawer);
        super.onCreate(savedInstanceState);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setCurrentItem(Calendar.getInstance().get(Calendar.MONTH));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TransactionHistoryActivity.this, TransactionManagerActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
