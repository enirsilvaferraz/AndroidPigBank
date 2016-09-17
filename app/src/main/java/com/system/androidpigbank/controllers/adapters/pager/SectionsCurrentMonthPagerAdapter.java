package com.system.androidpigbank.controllers.adapters.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.system.androidpigbank.controllers.fragments.CardFragmentImpl;
import com.system.androidpigbank.controllers.helpers.constant.Constants;

public class SectionsCurrentMonthPagerAdapter extends FragmentStatePagerAdapter {


    public SectionsCurrentMonthPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position) {

            case 0:
                fragment = CardFragmentImpl.newInstance(Constants.FRAGMENT_ID_SUMMARY_CATEGORY);
                break;

            case 1:
                fragment = CardFragmentImpl.newInstance(Constants.FRAGMENT_ID_TRANSACTION);
                break;

            case 2:
                fragment =  CardFragmentImpl.newInstance(Constants.FRAGMENT_ID_MONTH);
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
}