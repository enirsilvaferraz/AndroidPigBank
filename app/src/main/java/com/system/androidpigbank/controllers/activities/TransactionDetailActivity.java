package com.system.androidpigbank.controllers.activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.managers.LoaderResult;
import com.system.androidpigbank.controllers.managers.ManagerHelper;
import com.system.androidpigbank.helpers.constants.Constants;
import com.system.androidpigbank.models.business.CategoryBusiness;
import com.system.androidpigbank.models.entities.Category;

import java.util.ArrayList;
import java.util.List;

public class TransactionDetailActivity extends BaseActivity {

    private View container;
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


        ManagerHelper.execute(this, new ManagerHelper.LoaderResultInterface<List<Category>>() {
            @Override
            public List<Category> executeAction() throws Exception {
                return new CategoryBusiness(TransactionDetailActivity.this).getChartDataByMonth(month);
            }

            @Override
            public int loaderId() {
                return Constants.LOADER_DEFAULT_ID;
            }

            @Override
            public void onComplete(LoaderResult<List<Category>> data) {
                PieChart mChart = (PieChart) findViewById(R.id.chart);
                if (data.isSuccess()) {

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
                    mChart.setVisibility(View.GONE);
                    Snackbar.make(container, data.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
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
            final Category category = categories.get(i);
            if (category.getAmount() > 0) {
                yVals1.add(new Entry(category.getAmount(), i));
                xVals.add(category.getName());
            }
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

    @Override
    protected View getContainer() {
        return container;
    }


}
