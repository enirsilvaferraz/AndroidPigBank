package com.system.androidpigbank.controllers.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.system.androidpigbank.controllers.fragments.CardFragmentImpl;
import com.system.androidpigbank.controllers.helpers.Constants;

public class SectionsCurrentMonthPagerAdapter extends FragmentStatePagerAdapter {


    public SectionsCurrentMonthPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position) {

            case Constants.FRAGMENT_ID_SUMMARY_CATEGORY:
                fragment = CardFragmentImpl.newInstance(Constants.FRAGMENT_ID_SUMMARY_CATEGORY);
                break;

            case Constants.FRAGMENT_ID_ESTIMATE:
                fragment = CardFragmentImpl.newInstance(Constants.FRAGMENT_ID_ESTIMATE);
                break;

            case Constants.FRAGMENT_ID_TRANSACTION:
                fragment = CardFragmentImpl.newInstance(Constants.FRAGMENT_ID_TRANSACTION);
                break;

            case Constants.FRAGMENT_ID_MONTH:
                fragment =  CardFragmentImpl.newInstance(Constants.FRAGMENT_ID_MONTH);
                break;

        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return String.valueOf(position);
    }
}