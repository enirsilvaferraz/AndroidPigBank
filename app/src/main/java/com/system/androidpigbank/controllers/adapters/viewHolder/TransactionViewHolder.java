package com.system.androidpigbank.controllers.adapters.viewHolder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.system.androidpigbank.R;
import com.system.androidpigbank.architecture.viewHolders.ViewHolderAbs;
import com.system.androidpigbank.controllers.activities.TransactionManagerActivity;
import com.system.androidpigbank.helpers.constant.Constants;
import com.system.androidpigbank.models.entities.EntityAbs;
import com.system.androidpigbank.models.entities.Transaction;
import com.system.androidpigbank.views.RoundedTextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;

public class TransactionViewHolder extends ViewHolderAbs {

    private View containerAct;
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
        final NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumFractionDigits(2);

        textValue.setText("R$ " + numberFormat.format(transaction.getValue()));
        roundedTextView.setTextView(transaction.getCategory().getName());
        textContent.setText(transaction.getContent());
        textDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(transaction.getDate()));

        roundedTextView.setColor(((Transaction) model).getCategory().getColor());

        String categoryName = transaction.getCategory().getName();
        if (transaction.getCategorySecondary() !=null){
            categoryName +=  " / " + transaction.getCategorySecondary().getName();
        }

        textCategory.setText(categoryName);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                editCard((Transaction) model);
            }
        });
    }

    private void editCard(final Transaction transaction) {
        final Intent intent = new Intent(getActivity(), TransactionManagerActivity.class);
        intent.putExtra(Constants.BUNDLE_MODEL_DEFAULT, transaction);
        getActivity().startActivity(intent);
    }
}