package com.system.androidpigbank.controllers.adapters;

import android.view.View;
import android.widget.TextView;

import com.system.androidpigbank.R;
import com.system.androidpigbank.models.entities.EntityAbs;
import com.system.androidpigbank.models.entities.Transaction;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;

/**
 * Created by eferraz on 05/12/15.
 */
public class TransactionAdapter extends AdapterAbs {

    @Override
    @SuppressWarnings("ResourceType")
    protected int getResourceId() {
        return R.layout.view_holder_transaction;
    }

    @Override
    protected ViewHolderAbs getViewHolderInstance(View v) {
        return new TransactionViewHolder(v);
    }

    private class TransactionViewHolder extends ViewHolderAbs {

        private TextView textDate;
        private TextView textDateLabel;
        private TextView textValue;
        private TextView textValueLabel;
        private TextView textCategory;
        private TextView textCategoryLabel;
        private TextView textContent;
        private TextView textContentLabel;

        public TransactionViewHolder(View v) {
            super(v);

            textDate = (TextView) v.findViewById(R.id.item_transaction_date);
            textDateLabel = (TextView) v.findViewById(R.id.item_transaction_date_label);
            textValue = (TextView) v.findViewById(R.id.item_transaction_value);
            textValueLabel = (TextView) v.findViewById(R.id.item_transaction_value_label);
            textCategory = (TextView) v.findViewById(R.id.item_transaction_category);
            textCategoryLabel = (TextView) v.findViewById(R.id.item_transaction_category_label);
            textContent = (TextView) v.findViewById(R.id.item_transaction_content);
            textContentLabel = (TextView) v.findViewById(R.id.item_transaction_content_label);

            v.setOnClickListener(new View.OnClickListener() {

                boolean clicked = true;

                @Override
                public void onClick(View v) {

                    if(clicked){

                        textDateLabel.setVisibility(View.VISIBLE);
                        textValueLabel.setVisibility(View.VISIBLE);
                        textCategoryLabel.setVisibility(View.VISIBLE);

                        if(!textContent.getText().toString().trim().isEmpty()) {
                            textContent.setVisibility(View.VISIBLE);
                            textContentLabel.setVisibility(View.VISIBLE);
                        }
                    }

                    else {

                        textDateLabel.setVisibility(View.GONE);
                        textValueLabel.setVisibility(View.GONE);
                        textCategoryLabel.setVisibility(View.GONE);
                        textContent.setVisibility(View.GONE);
                        textContentLabel.setVisibility(View.GONE);
                    }

                    clicked = !clicked;
                }
            });
        }

        @Override
        public void bind(EntityAbs model) {

            Transaction transaction = (Transaction) model;
            final NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMinimumFractionDigits(2);

            textDate.setText(new SimpleDateFormat("d MMMM, yyyy").format(transaction.getDate()));
            textValue.setText("R$ " + numberFormat.format(transaction.getValue()));
            textCategory.setText(transaction.getCategory().getName());
            textContent.setText(transaction.getContent());

            textDateLabel.setVisibility(View.GONE);
            textValueLabel.setVisibility(View.GONE);
            textCategoryLabel.setVisibility(View.GONE);
            textContent.setVisibility(View.GONE);
            textContentLabel.setVisibility(View.GONE);
        }
    }
}