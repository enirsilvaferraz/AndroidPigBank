package com.system.androidpigbank.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.system.androidpigbank.R;
import com.system.androidpigbank.models.sqlite.entities.Category;
import com.system.architecture.utils.JavaUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Enir on 10/09/2016.
 */

public class CategorySummaryView extends LinearLayout {

    @BindView(R.id.item_category_progress)
    ProgressBar pbProgress;

    @BindView(R.id.item_category_progress_value)
    TextView tvProgressValue;

    @BindView(R.id.item_category_name)
    TextView tvName;

    @BindView(R.id.item_category_range)
    TextView tvRange;

    public CategorySummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.item_view_holder_category_summary, this);
        ButterKnife.bind(this);
    }

    public void bind(Category category) {
        tvName.setText(category.getName());
        tvRange.setText(JavaUtils.NumberUtil.currencyFormat(category.getAmount()) + " de " + JavaUtils.NumberUtil.currencyFormat(0D));
        tvProgressValue.setText("N/A");
        pbProgress.setProgress(0);
    }
}
