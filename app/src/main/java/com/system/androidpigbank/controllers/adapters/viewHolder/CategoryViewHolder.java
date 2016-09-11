package com.system.androidpigbank.controllers.adapters.viewHolder;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.behaviors.HighlightCardBehavior;
import com.system.androidpigbank.models.sqlite.entities.Category;
import com.system.androidpigbank.models.sqlite.entities.Transaction;
import com.system.androidpigbank.views.DividerView;
import com.system.androidpigbank.views.TransactionView;
import com.system.architecture.adapters.CardAdapter;
import com.system.architecture.adapters.CardViewHolder;
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

    @BindView(R.id.item_category_progress)
    ProgressBar progressBar;

    @BindView(R.id.item_category_progress_value)
    TextView tvProgressValue;

    @BindView(R.id.item_category_transaction_container)
    LinearLayout transactionContainer;

    Context context;

    public CategoryViewHolder(View itemView, boolean isCardMode) {
        super(itemView, isCardMode);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
    }

    @Override
    public void bind(CardAdapter.CardModel model, OnClickListener onClickListener) {
        super.bind(model, onClickListener);

        Category item = (Category) model;

        tvName.setText(item.getName());

        tvRange.setText(JavaUtils.NumberUtil.currencyFormat(item.getAmount()) + " de " +
                JavaUtils.NumberUtil.currencyFormat(0D));
        tvProgressValue.setText("N/A");
        progressBar.setProgress(0);
    }
}