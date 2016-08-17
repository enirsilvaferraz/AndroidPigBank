package com.system.androidpigbank.views;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.system.androidpigbank.R;
import com.system.architecture.utils.JavaUtils;
import com.system.androidpigbank.models.entities.Transaction;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eferraz on 06/08/16.
 */

public class TransactionView extends LinearLayout {

    @BindView(R.id.item_transaction_content)
    TextView tvContent;

    @BindView(R.id.item_transaction_date)
    TextView tvDate;

    @BindView(R.id.item_transaction_category)
    TextView tvCategory;

    @BindView(R.id.item_transaction_value)
    TextView tvValue;

    public TransactionView(Context context) {
        super(context);
        inflate(getContext(), R.layout.view_transaction, this);
        ButterKnife.bind(this);
    }

    public TransactionView(Context context, Transaction transaction) {
        this(context);
        bind(transaction);
    }

    public void bind(Transaction transaction) {

        tvValue.setText(JavaUtils.NumberUtil.currencyFormat(transaction.getValue()));
        tvContent.setText(transaction.getContent());
        tvDate.setText(JavaUtils.DateUtil.format(transaction.getDate()));
        tvCategory.setText(transaction.getCategory().getName());
    }
}
