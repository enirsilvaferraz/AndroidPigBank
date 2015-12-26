package com.system.androidpigbank.controllers.activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.managers.CategoryManager;
import com.system.androidpigbank.controllers.managers.LoaderResult;
import com.system.androidpigbank.helpers.Constants;
import com.system.androidpigbank.models.entities.Category;

import java.util.ArrayList;
import java.util.Arrays;
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
        //chart = (BarChart) findViewById(R.id.chart);

        month = getIntent().getIntExtra("MONTH", 0);

        getSupportLoaderManager().restartLoader(Constants.LOADER_CATEGORY, null, this);
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

            PieChart mChart = (PieChart) findViewById(R.id.chart);
            mChart.setUsePercentValues(true);
            mChart.setDescription("");
            mChart.setRotationEnabled(true);

            setData(data.getData(), 100, mChart);

            mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

            Legend legend = mChart.getLegend();
            legend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
            legend.setXEntrySpace(7f);
            legend.setYEntrySpace(0f);
            legend.setYOffset(0f);

        } else {
            chart.setVisibility(View.GONE);
            Snackbar.make(container, data.getException().getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<LoaderResult<List<Category>>> loader) {

    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }

    private void setData(List<Category> categories, float range, Chart mChart) {

        ArrayList<Entry> yVals1 = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();

        for (int i = 0; i < categories.size(); i++) {
            yVals1.add(new Entry(categories.get(i).getAmount(), i));
            xVals.add(categories.get(i).getName());
        }

        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(5f);

        dataSet.setColors(getColors());

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);

        mChart.setData(data);

        mChart.highlightValues(null);
        mChart.invalidate();
    }

    @NonNull
    private ArrayList<Integer> getColors() {

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        return colors;
    }
}
