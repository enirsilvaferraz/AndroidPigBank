package com.system.androidpigbank.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
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

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomPercentualProgress, 0, 0);
        try {
            mValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, a.getDimensionPixelSize(R.styleable.CustomPercentualProgress_percentTextSize, 16));
            mValue.setTextColor(a.getColor(R.styleable.CustomPercentualProgress_percentTextColor, context.getColor(R.color.material_white)));
            mProgress.setProgressDrawable(a.getDrawable(R.styleable.CustomPercentualProgress_percentProgressDrawable));
        } finally {
            a.recycle();
        }

    }

    public void bind(Double maxValue, Double totalValue) {

        Long percentual = Math.round(totalValue * 100 / (maxValue == 0 ? 1 : maxValue));

        mProgress.setProgress(percentual.intValue());
        mValue.setText(percentual + "%");

    }
}
