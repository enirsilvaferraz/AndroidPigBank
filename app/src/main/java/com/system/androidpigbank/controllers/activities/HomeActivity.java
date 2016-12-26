package com.system.androidpigbank.controllers.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.adapters.SectionsCurrentMonthPagerAdapter;
import com.system.androidpigbank.controllers.helpers.Constants;
import com.system.androidpigbank.controllers.helpers.IntentRouter;
import com.system.androidpigbank.controllers.vos.HomeObjectVO;
import com.system.androidpigbank.models.firebase.business.HomeBusiness;
import com.system.androidpigbank.views.CustomHeaderSummary;
import com.system.architecture.activities.BaseActivity;
import com.system.androidpigbank.controllers.fragments.CardFragmentAbs;
import com.system.architecture.utils.JavaUtils;
import com.system.architecture.utils.behaviors.ScrollAwareFABBehavior;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity {

    private static final List<String> ACCESS_PERMISSIONS = Arrays.asList(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    private static final int HOME_INDICATOR = 1;

    @BindView(R.id.container)
    ViewPager mViewPager;

    @BindView(R.id.fab)
    FloatingActionButton fab;

//    @BindView(R.id.toolbar)
//    Toolbar toolbar;

    @BindView(R.id.home_header)
    CustomHeaderSummary homeHeader;

    @BindView(R.id.collapse_layout)
    CollapsingToolbarLayout collapseLayout;

    @BindView(R.id.appbar)
    AppBarLayout appbar;

    private HomeObjectVO data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_home_tab);
        ButterKnife.bind(this);

        //toolbar.setTitle("");
        //setSupportActionBar(toolbar);

        appbar.setExpanded(false);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        //params.setBehavior(new ScrollAwareFABBehavior(this, null));
        fab.requestLayout();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewPager.getCurrentItem() == 1){
                    IntentRouter.startEstimateManager(HomeActivity.this, null);
                } else {
                    IntentRouter.startTransactionManager(HomeActivity.this, null);
                }
            }
        });

        if (savedInstanceState == null) {
            Calendar calendar = Calendar.getInstance();
            update(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
        } else {
            data = savedInstanceState.getParcelable(HomeObjectVO.class.getSimpleName());
            if (data == null) {
                data = new HomeObjectVO();
            }
            configureResult(data);
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof CardFragmentAbs) {
            switch (((CardFragmentAbs) fragment).getFragmentID()) {
                case Constants.FRAGMENT_ID_SUMMARY_CATEGORY:
                    ((CardFragmentAbs) fragment).setData(data.getListCategorySummary());
                    break;

                case Constants.FRAGMENT_ID_ESTIMATE:
                    ((CardFragmentAbs) fragment).setData(data.getListEstimate());
                    break;

                case Constants.FRAGMENT_ID_TRANSACTION:
                    ((CardFragmentAbs) fragment).setData(data.getListTransaction());
                    break;

                case Constants.FRAGMENT_ID_MONTH:
                    ((CardFragmentAbs) fragment).setData(data.getListMonth());
                    break;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(HomeObjectVO.class.getSimpleName(), data);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (RESULT_OK == resultCode && (Constants.REQUEST_ACTION_DELETE == requestCode || Constants.REQUEST_ACTION_SAVE == requestCode)) {
            update(this.data.getCurrentMonth().getMonth(), this.data.getCurrentMonth().getYear());
            showMessage(data.getIntExtra(Constants.BUNDLE_MESSAGE_ID, 0));
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void callApi() {
        update(this.data.getCurrentMonth().getMonth(), this.data.getCurrentMonth().getYear());
    }

    public void update(final int month, final int year) {

        final ProgressDialog dialog = ProgressDialog.show(HomeActivity.this, "", "Loading. Please wait...", true);

        new HomeBusiness().findAll(month, year, new HomeBusiness.SingleResult() {
            @Override
            public void onFind(HomeObjectVO homeObjectVO) {
                configureResult(homeObjectVO);
                dialog.dismiss();
            }

            @Override
            public void onError(String error) {
                dialog.dismiss();
                showMessage(error);
            }
        });
    }

    private void configureResult(HomeObjectVO data) {
        this.data = data;

        if (data != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, data.getCurrentMonth().getYear());
            calendar.set(Calendar.MONTH, data.getCurrentMonth().getMonth());

            appbar.setExpanded(false);
            collapseLayout.setTitle(JavaUtils.DateUtil.format(calendar.getTime(), JavaUtils.DateUtil.MMMM_DE_YYYY));

            homeHeader.bind(data.getCurrentMonth().getValue(), data.getCurrentMonth().getPlannedValue());
        }

        SectionsCurrentMonthPagerAdapter adapter = new SectionsCurrentMonthPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setCurrentItem(HOME_INDICATOR);
        mViewPager.getAdapter().notifyDataSetChanged();
    }
}
