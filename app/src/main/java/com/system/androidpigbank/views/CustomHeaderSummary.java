package com.system.androidpigbank.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.system.androidpigbank.R;
import com.system.architecture.utils.JavaUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Enir on 24/09/2016.
 */

public class CustomHeaderSummary extends LinearLayout {

    @BindView(R.id.custom_header_registrados)
    TextView tvRegistrados;

    @BindView(R.id.custom_header_planejados)
    TextView tvPlanejados;

    @BindView(R.id.custom_header_total)
    TextView tvTotal;

    @BindView(R.id.custom_header_progress)
    ProgressBar pProgress;

    @BindView(R.id.custom_header_progress_value)
    TextView pProgressValue;

    public CustomHeaderSummary(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.custom_header_summary, this);
        ButterKnife.bind(this);
    }

    public void bind(double registrados, double planejados) {

        Double total = planejados - registrados;
        Integer percentual = Double.valueOf(registrados * 100 / (planejados == 0 ? 1 : planejados)).intValue();

        tvRegistrados.setText(JavaUtils.NumberUtil.currencyFormat(registrados));
        tvPlanejados.setText(JavaUtils.NumberUtil.currencyFormat(planejados));
        tvTotal.setText(JavaUtils.NumberUtil.currencyFormat(total));

        pProgress.setProgress(percentual);
        pProgressValue.setText(percentual + "%");
    }
}
