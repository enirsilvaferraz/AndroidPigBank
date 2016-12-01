package com.system.androidpigbank.controllers.adapters.recyclerv.viewHolder;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.vos.CategoryVO;
import com.system.androidpigbank.views.CustomPercentualProgress;
import com.system.architecture.activities.CardAdapterAbs;
import com.system.architecture.activities.CardViewHolder;
import com.system.architecture.utils.JavaUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryViewHolder extends CardViewHolder {

    @BindView(R.id.card_view_container)
    CardView cvContainer;

    @BindView(R.id.item_category_name)
    TextView tvName;

    @BindView(R.id.item_category_range)
    TextView tvRange;

    @BindView(R.id.item_category_progress_component)
    CustomPercentualProgress progress;

    Context context;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
    }

    @Override
    public void bind(final CardAdapterAbs.CardModel model, final OnClickListener onClickListener) {
        super.bind(model, onClickListener);

        CategoryVO item = (CategoryVO) model;

        tvName.setText(item.getName());

        tvRange.setText(JavaUtils.NumberUtil.currencyFormat(item.getAmount()));

        progress.setVisibility(View.GONE);
        progress.bind(item.getMonthVO().getValue(), item.getAmount());

    }
}