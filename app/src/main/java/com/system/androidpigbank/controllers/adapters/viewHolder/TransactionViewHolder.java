package com.system.androidpigbank.controllers.adapters.viewHolder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.helpers.IntentRouter;
import com.system.architecture.utils.JavaUtils;
import com.system.architecture.viewHolders.ViewHolderAbs;
import com.system.androidpigbank.controllers.activities.TransactionManagerActivity;
import com.system.androidpigbank.controllers.helpers.constant.Constants;
import com.system.androidpigbank.models.entities.EntityAbs;
import com.system.androidpigbank.models.entities.Transaction;
import com.system.androidpigbank.views.RoundedTextView;

public class TransactionViewHolder extends ViewHolderAbs {

    private TextView textValue;
    private TextView textCategory;
    private TextView textContent;
    private TextView textDate;
    private RoundedTextView roundedTextView;

    public TransactionViewHolder(View v, AppCompatActivity activity, RecyclerView.Adapter adapter) {
        super(v, activity, adapter);

        textValue = (TextView) v.findViewById(R.id.item_transaction_value);
        textCategory = (TextView) v.findViewById(R.id.item_transaction_category);
        textContent = (TextView) v.findViewById(R.id.item_transaction_content);
        textDate = (TextView) v.findViewById(R.id.item_transaction_date);

        roundedTextView = (RoundedTextView) v.findViewById(R.id.item_transaction_rounded_view);
    }

    @Override
    public void bind(final EntityAbs model) {

        final Transaction transaction = (Transaction) model;

        textValue.setText(JavaUtils.NumberUtil.currencyFormat(transaction.getValue()));
        roundedTextView.setTextView(transaction.getCategory().getName());
        textContent.setText(transaction.getContent());
        textDate.setText(JavaUtils.DateUtil.format(transaction.getDate()));

        roundedTextView.setColor(((Transaction) model).getCategory().getColor());

        String categoryName = transaction.getCategory().getName();
        if (transaction.getCategorySecondary() != null) {
            categoryName += " / " + transaction.getCategorySecondary().getName();
        }

        textCategory.setText(categoryName);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                IntentRouter.startTransactionManager(getActivity(), transaction);
            }
        });
    }
}