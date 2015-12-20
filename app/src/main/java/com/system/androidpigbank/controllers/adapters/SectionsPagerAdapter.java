package com.system.androidpigbank.controllers.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.system.androidpigbank.controllers.fragments.PlaceholderFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return PlaceholderFragment.newInstance(position);
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