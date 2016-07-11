package com.system.androidpigbank.controllers.adapters.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.system.androidpigbank.controllers.fragments.CategorySummaryFragment;
import com.system.androidpigbank.controllers.fragments.TransactionListFragment;

import java.util.Calendar;

public class SectionsCurrentMonthPagerAdapter extends FragmentPagerAdapter {

    public SectionsCurrentMonthPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = CategorySummaryFragment.newInstance(Calendar.getInstance().get(Calendar.MONTH));
                break;

            case 1:
                fragment = TransactionListFragment.newInstance(Calendar.getInstance().get(Calendar.MONTH));
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return String.valueOf(position);
    }
}