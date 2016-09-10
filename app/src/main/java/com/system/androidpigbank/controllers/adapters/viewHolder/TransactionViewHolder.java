package com.system.androidpigbank.controllers.adapters.viewHolder;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.system.androidpigbank.R;
import com.system.architecture.adapters.CardAdapter;
import com.system.androidpigbank.models.sqlite.entities.Transaction;
import com.system.androidpigbank.views.RoundedImageView;
import com.system.architecture.adapters.ViewHolderModel;
import com.system.architecture.utils.JavaUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionViewHolder extends ViewHolderModel {

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
    public void bind(CardAdapter.CardModel model, final OnClickListener onClickListener) {
        final Transaction transaction = (Transaction) model;

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

        if (onClickListener != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onContainerClicked(transaction);
                }
            });
        }
    }
}