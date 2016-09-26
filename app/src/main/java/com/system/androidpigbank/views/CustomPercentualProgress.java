package com.system.androidpigbank.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.system.androidpigbank.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eferraz on 26/09/16.
 */

public class CustomPercentualProgress extends LinearLayout {

    @BindView(R.id.custom_progress_draw)
    ProgressBar mProgress;

    @BindView(R.id.custom_progress_value)
    TextView mValue;

    public CustomPercentualProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.custom_percentual_progress, this);
        ButterKnife.bind(this);
    }

    public void bind(Double maxValue, Double totalValue) {

        Integer percentual = Double.valueOf(totalValue * 100 / (maxValue == 0 ? 1 : maxValue)).intValue();

        mProgress.setProgress(percentual);
        mValue.setText(String.format("d%", percentual));
    }
}
