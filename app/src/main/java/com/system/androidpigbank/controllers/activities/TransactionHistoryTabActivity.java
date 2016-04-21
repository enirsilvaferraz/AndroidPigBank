package com.system.androidpigbank.controllers.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.adapters.SectionsPagerAdapter;
import com.system.androidpigbank.models.business.BackupService;

import java.util.Calendar;

public class TransactionHistoryTabActivity extends BaseActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        //getWindow().setExitTransition(new Explode());
        getWindow().setSharedElementExitTransition(new ChangeBounds());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history_tab);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setCurrentItem(Calendar.getInstance().get(Calendar.MONTH));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(view -> {
//            startActivity(new Intent(TransactionHistoryTabActivity.this, TransactionManagerActivity.class));
//
//            //new TransactionManagerDialog().show(getSupportFragmentManager(), "TAG");
//        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TransactionHistoryTabActivity.this, TransactionManagerActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(new Intent(this, BackupService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_transaction_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.transaction_history_act_detail) {
            final Intent intent = new Intent(this, TransactionDetailActivity.class);
            intent.putExtra("MONTH", mViewPager.getCurrentItem());
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected View getContainer() {
        return mViewPager;
    }
}