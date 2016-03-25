package com.system.androidpigbank.controllers.vIewHolders;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.activities.TransactionManagerActivity;
import com.system.androidpigbank.helpers.Constants;
import com.system.androidpigbank.models.entities.EntityAbs;
import com.system.androidpigbank.models.entities.Transaction;
import com.system.androidpigbank.views.RoundedTextView;

import java.text.NumberFormat;

public class TransactionViewHolder extends ViewHolderAbs {

    private View containerAct;
    private TextView textValue;
    private TextView textCategory;
    private TextView textContent;
    private RoundedTextView roundedTextView;

    public TransactionViewHolder(View v, AppCompatActivity activity, RecyclerView.Adapter adapter) {
        super(v, activity, adapter);

        textValue = (TextView) v.findViewById(R.id.item_transaction_value);
        textCategory = (TextView) v.findViewById(R.id.item_transaction_category);
        textContent = (TextView) v.findViewById(R.id.item_transaction_content);

        roundedTextView = (RoundedTextView) v.findViewById(R.id.item_transaction_rounded_view);
    }

    @Override
    public void bind(final EntityAbs model) {

        final Transaction transaction = (Transaction) model;
        final NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumFractionDigits(2);

        roundedTextView.setTransitionName("TRNAME"+((Transaction) model).getId());

        textValue.setText("R$ " + numberFormat.format(transaction.getValue()));
        textCategory.setText(transaction.getCategory().getName());
        roundedTextView.setTextView(transaction.getCategory().getName());
        textContent.setText(transaction.getContent());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                editCard((Transaction) model);
            }
        });
    }

    private void editCard(final Transaction transaction) {
        final Intent intent = new Intent(getActivity(), TransactionManagerActivity.class);
        intent.putExtra(Constants.BUNDLE_TRANSACTION, transaction);
        intent.putExtra("TR_NAME", roundedTextView.getTransitionName());

//        getActivity().startActivityForResult(intent, Constants.REQUEST_TRANSACTION_EDIT);

        getActivity().startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), roundedTextView, roundedTextView.getTransitionName()).toBundle());
    }
}