package com.system.androidpigbank.controllers.adapters.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.system.androidpigbank.controllers.adapters.recyclerv.MonthAdapter;
import com.system.androidpigbank.controllers.fragments.CategorySummaryFragment;
import com.system.androidpigbank.controllers.fragments.MonthFragment;
import com.system.androidpigbank.controllers.fragments.TransactionListFragment;

import java.util.Calendar;

public class SectionsCurrentMonthPagerAdapter extends FragmentPagerAdapter {

    private CategorySummaryFragment categoryFragment;
    private TransactionListFragment transactionFragment;
    private MonthFragment monthFragment;

    private MonthAdapter.OnItemClicked onItemClicked;

    public SectionsCurrentMonthPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Integer month = Calendar.getInstance().get(Calendar.MONTH);
        Integer year = Calendar.getInstance().get(Calendar.YEAR);

        if (categoryFragment == null) {
            categoryFragment = CategorySummaryFragment.newInstance(month, year);
        }

        if (transactionFragment == null) {
            transactionFragment = TransactionListFragment.newInstance(month, year);
        }

        if (monthFragment == null) {
            monthFragment = MonthFragment.newInstance(year);
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

    public void setCurrentTime(int month, int year) {
        categoryFragment.update(month, year);
        transactionFragment.update(month, year);
    }
}