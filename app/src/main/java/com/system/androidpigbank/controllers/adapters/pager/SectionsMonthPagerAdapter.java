package com.system.androidpigbank.controllers.adapters.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.system.androidpigbank.controllers.fragments.TransactionListFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SectionsMonthPagerAdapter extends FragmentPagerAdapter {

    public SectionsMonthPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return TransactionListFragment.newInstance(position, 2016);
    }

    @Override
    public int getCount() {
        return 12;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, position);
        return new SimpleDateFormat("MMMM").format(cal.getTime());
    }
}