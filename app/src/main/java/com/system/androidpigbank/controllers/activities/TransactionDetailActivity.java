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
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class TransactionDetailActivity extends BaseActivity<List<Category>> {

    private View container;
    private BarChart chart;
    private int month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        container = findViewById(R.id.transaction_detail_container);
        chart = (BarChart) findViewById(R.id.chart);

        month = getIntent().getIntExtra("MONTH", 0);

        getSupportLoaderManager().initLoader(Constants.LOADER_CATEGORY, null, this);
    }

    private BarDataSet getData(BarEntry object, String x) {
        List<BarEntry> list = new ArrayList<>();
        list.add(object);
        return new BarDataSet(list, x);
    }

    @Override
    public Loader<LoaderResult<List<Category>>> onCreateLoader(int id, Bundle args) {
        return new CategoryManager(this).getChartDataByMonth(month);
    }

    @Override
    public void onLoadFinished(Loader<LoaderResult<List<Category>>> loader, LoaderResult<List<Category>> data) {

        if (data.isSuccess()) {

//            final int[] intArray = getResources().getIntArray(R.array.material_colors);
            final int[] intArray = {R.color.material_blue, R.color.material_red, R.color.material_light_green};
            Arrays.sort(intArray);

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, month);

            BarData barData = new BarData();
            barData.addXValue(new SimpleDateFormat("MMMM").format(cal.getTime()));

            for (Category category : data.getData()) {
                final BarDataSet dataSet = getData(new BarEntry(category.getAmount(), 0), category.getName());
                dataSet.setColors(intArray);
                barData.addDataSet(dataSet);
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
