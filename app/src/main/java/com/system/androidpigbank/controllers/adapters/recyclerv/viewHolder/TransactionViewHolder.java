package com.system.androidpigbank.controllers.adapters.recyclerv.viewHolder;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.vos.TransactionVO;
import com.system.architecture.activities.CardAdapterAbs;
import com.system.androidpigbank.views.RoundedImageView;
import com.system.architecture.activities.CardViewHolder;
import com.system.architecture.utils.JavaUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionViewHolder extends CardViewHolder {

    @BindView(R.id.item_bar_container)
    LinearLayout llContainer;

    @BindView(R.id.card_view_container)
    CardView cvContainer;

    @BindView(R.id.item_transaction_rounded_view)
    RoundedImageView ivPaymentType;

    @BindView(R.id.item_transaction_content)
    TextView textContent;

    @BindView(R.id.item_transaction_date)
    TextView textDate;

    @BindView(R.id.item_transaction_category)
    TextView textCategory;

    @BindView(R.id.item_transaction_value)
    TextView textValue;

    public TransactionViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(CardAdapterAbs.CardModel model, final OnClickListener onClickListener) {
        super.bind(model, onClickListener);

        final TransactionVO transaction = (TransactionVO) model;

        textValue.setText(JavaUtils.NumberUtil.currencyFormat(transaction.getValue()));
        textContent.setText(transaction.getContent());
        textDate.setText(JavaUtils.DateUtil.format(transaction.getDatePayment()));

        if (transaction.getPaymentType() != null) {
            ivPaymentType.setImageView(transaction.getPaymentType().getResId());
        } else {
            ivPaymentType.setImageView(null);
        }

        String categoryName = transaction.getCategory().getName();
        if (transaction.getCategorySecondary() != null) {
            categoryName += " / " + transaction.getCategorySecondary().getName();
        }

        textCategory.setText(categoryName);
    }
}