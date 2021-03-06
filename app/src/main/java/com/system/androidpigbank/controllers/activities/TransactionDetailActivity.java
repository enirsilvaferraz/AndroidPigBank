package com.system.androidpigbank.controllers.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.managers.CategoryManager;
import com.system.androidpigbank.controllers.managers.LoaderResult;
import com.system.androidpigbank.helpers.Constants;
import com.system.androidpigbank.models.entities.Category;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionDetailActivity extends BaseActivity<List<Category>> {

    private View container;
    private BarChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        container = findViewById(R.id.transaction_detail_container);

        chart = (BarChart) findViewById(R.id.chart);
        getSupportLoaderManager().initLoader(Constants.LOADER_CATEGORY, null, this);
    }

    private BarDataSet getData(BarEntry object, String x) {
        List<BarEntry> list = new ArrayList<>();
        list.add(object);
        return new BarDataSet(list, x);
    }

    @Override
    public Loader<LoaderResult<List<Category>>> onCreateLoader(int id, Bundle args) {
        return new CategoryManager(this).getCurrentMonth();
    }

    @Override
    public void onLoadFinished(Loader<LoaderResult<List<Category>>> loader, LoaderResult<List<Category>> data) {

        if (data.isSuccess()) {

            BarData barData = new BarData();
            barData.addXValue(new SimpleDateFormat("MMMM").format(new Date()));

            for (Category category : data.getData()) {
                barData.addDataSet(getData(new BarEntry(category.getAmount(), 0), category.getName()));
            }

            chart.setData(barData);

        } else {
            chart.setVisibility(View.GONE);
            Snackbar.make(container, data.getException().getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<LoaderResult<List<Category>>> loader) {

    }
}
