package com.system.androidpigbank.controllers.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.system.androidpigbank.models.entities.Category;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eferraz on 06/08/16.
 */

@Deprecated
public class ChartFragment extends Fragment {

    @BindView(R.id.chart)
    PieChart mChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_transaction_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public static ChartFragment newInstance() {
        ChartFragment fragment = new ChartFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void update(List<Category> data) {

        mChart.setUsePercentValues(true);
        mChart.setDescription("");
        mChart.setRotationEnabled(false);

        setData(data, 100, mChart);

//        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend legend = mChart.getLegend();
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
        legend.setXEntrySpace(7f);
        legend.setYEntrySpace(0f);
        legend.setYOffset(0f);

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
