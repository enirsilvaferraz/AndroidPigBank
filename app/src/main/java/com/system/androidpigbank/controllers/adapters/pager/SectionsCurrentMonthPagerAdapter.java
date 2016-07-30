package com.system.androidpigbank.controllers.adapters.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.system.androidpigbank.controllers.adapters.recyclerv.MonthAdapter;
import com.system.androidpigbank.controllers.fragments.CategorySummaryFragment;
import com.system.androidpigbank.controllers.fragments.MonthFragment;
import com.system.androidpigbank.controllers.fragments.TransactionListFragment;
import com.system.androidpigbank.controllers.managers.LoaderResult;
import com.system.androidpigbank.controllers.vos.HomeObject;

import java.util.Calendar;

public class SectionsCurrentMonthPagerAdapter extends FragmentStatePagerAdapter {

    private CategorySummaryFragment categoryFragment;
    private TransactionListFragment transactionFragment;
    private MonthFragment monthFragment;

    private MonthAdapter.OnItemClicked onItemClicked;

    public SectionsCurrentMonthPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if (categoryFragment == null) {
            categoryFragment = CategorySummaryFragment.newInstance();
        }

        if (transactionFragment == null) {
            transactionFragment = TransactionListFragment.newInstance();
        }

        if (monthFragment == null) {
            monthFragment = MonthFragment.newInstance();
            monthFragment.setOnItemClicked(onItemClicked);
        }

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = categoryFragment;
                break;

            case 1:
                fragment = transactionFragment;
                break;

            case 2:
                fragment = monthFragment;
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return String.valueOf(position);
    }

    public void setOnItemClicked(MonthAdapter.OnItemClicked onItemClicked) {
        this.onItemClicked = onItemClicked;
    }

    public void update(HomeObject data) {

        if (categoryFragment != null) {
            categoryFragment.update(data.getListCategorySummary());
        }

        if (categoryFragment != null) {
            transactionFragment.update(data.getListTransaction());
        }

        if (categoryFragment != null) {
            monthFragment.update(data.getListMonth());
        }
    }

    @Override
    public int getItemPosition(Object object) {
        // Causes adapter to reload all Fragments when
        // notifyDataSetChanged is called
        return POSITION_NONE;
    }
}