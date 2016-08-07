package com.system.androidpigbank.controllers.adapters.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.system.androidpigbank.controllers.adapters.recyclerv.MonthAdapter;
import com.system.androidpigbank.controllers.fragments.CategorySummaryFragment;
import com.system.androidpigbank.controllers.fragments.ChartFragment;
import com.system.androidpigbank.controllers.fragments.MonthFragment;
import com.system.androidpigbank.controllers.fragments.TransactionListFragment;
import com.system.androidpigbank.controllers.vos.HomeObject;

public class SectionsCurrentMonthPagerAdapter extends FragmentStatePagerAdapter {

    private CategorySummaryFragment categoryFragment;
    private TransactionListFragment transactionFragment;
    private MonthFragment monthFragment;
    private ChartFragment chartFragment;

    private MonthAdapter.OnItemClicked onItemClicked;

    public SectionsCurrentMonthPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position) {

            case 0:

                if (categoryFragment == null) {
                    categoryFragment = CategorySummaryFragment.newInstance();
                }

                fragment = categoryFragment;
                break;

            case 1:

                if (transactionFragment == null) {
                    transactionFragment = TransactionListFragment.newInstance();
                }

                fragment = transactionFragment;
                break;

            case 2:

                if (monthFragment == null) {
                    monthFragment = MonthFragment.newInstance();
                    monthFragment.setOnItemClicked(onItemClicked);
                }

                fragment = monthFragment;
                break;

            case 3:

                if (chartFragment == null) {
                    chartFragment = ChartFragment.newInstance();
                }

                fragment = chartFragment;
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

        if (transactionFragment != null) {
            transactionFragment.update(data.getListTransaction());
        }

        if (monthFragment != null) {
            monthFragment.update(data.getListMonth());
        }

        if (chartFragment != null) {
            chartFragment.update(data.getListCategorySummary());
        }
    }

    @Override
    public int getItemPosition(Object object) {
        // Causes adapter to reload all Fragments when
        // notifyDataSetChanged is called
        return POSITION_NONE;
    }
}