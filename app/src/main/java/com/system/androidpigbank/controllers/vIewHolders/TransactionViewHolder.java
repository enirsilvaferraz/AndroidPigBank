package com.system.androidpigbank.controllers.vIewHolders;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.activities.BaseActivity;
import com.system.androidpigbank.controllers.activities.TransactionManagerActivity;
import com.system.androidpigbank.controllers.adapters.TransactionAdapter;
import com.system.androidpigbank.controllers.managers.LoaderResult;
import com.system.androidpigbank.controllers.managers.TransactionManager;
import com.system.androidpigbank.helpers.Constants;
import com.system.androidpigbank.models.entities.EntityAbs;
import com.system.androidpigbank.models.entities.Transaction;

import java.text.NumberFormat;

public class TransactionViewHolder extends ViewHolderAbs {

    private View containerAct;
    private TextView textValue;
    private TextView textValueLabel;
    private TextView textCategory;
    private TextView textCategoryLabel;
    private TextView textContent;
    private TextView textContentLabel;
    private ImageButton btnDelete;
    private ImageButton btnEdit;

    public TransactionViewHolder(View v, AppCompatActivity activity, RecyclerView.Adapter adapter) {
        super(v, activity, adapter);

        textValue = (TextView) v.findViewById(R.id.item_transaction_value);
        textValueLabel = (TextView) v.findViewById(R.id.item_transaction_value_label);
        textCategory = (TextView) v.findViewById(R.id.item_transaction_category);
        textCategoryLabel = (TextView) v.findViewById(R.id.item_transaction_category_label);
        textContent = (TextView) v.findViewById(R.id.item_transaction_content);
        textContentLabel = (TextView) v.findViewById(R.id.item_transaction_content_label);

        containerAct = v.findViewById(R.id.item_transaction_container_act);
        btnDelete = (ImageButton) v.findViewById(R.id.item_transaction_act_delete);
        btnEdit = (ImageButton) v.findViewById(R.id.item_transaction_act_edit);

        v.setOnClickListener(new View.OnClickListener() {
            boolean clicked = true;

            @Override
            public void onClick(View v) {

                if (clicked) {
                    openCard();
                } else {
                    closeCard();
                }
                clicked = !clicked;
            }
        });
    }

    private void deleteCard(final Transaction transaction) {

        LoaderManager.LoaderCallbacks<LoaderResult<Transaction>> callback = new LoaderManager.LoaderCallbacks<LoaderResult<Transaction>>() {

            @Override
            public Loader<LoaderResult<Transaction>> onCreateLoader(int id, Bundle args) {
                return new TransactionManager(itemView.getContext()).delete(transaction);
            }

            @Override
            public void onLoadFinished(Loader<LoaderResult<Transaction>> loader, LoaderResult<Transaction> data) {
                if (data.isSuccess()) {
                    ((TransactionAdapter) getAdapter()).removeItem(data.getData());
                } else {
                    ((BaseActivity) getActivity()).showMessage(data.getException());
                }
            }

            @Override
            public void onLoaderReset(Loader loader) {
            }
        };

        getActivity().getSupportLoaderManager().restartLoader(Constants.LOADER_TRANSACTION_DELETE, null, callback);
    }

    private void editCard(final Transaction transaction) {
        final Intent intent = new Intent(getActivity(), TransactionManagerActivity.class);
        intent.putExtra(Constants.BUNDLE_TRANSACTION, transaction);
        getActivity().startActivityForResult(intent, Constants.REQUEST_TRANSACTION_EDIT);
    }

    private void closeCard() {
        //                    DisplayMetrics displayMetrics = itemView.getContext().getResources().getDisplayMetrics();
//                    Float padding = 15 * displayMetrics.density;

        Float padding = 0F;

        GridLayoutManager.LayoutParams l = (GridLayoutManager.LayoutParams) ((CardView) itemView).getLayoutParams();
        l.setMargins(padding.intValue(), 0, padding.intValue(), 0);
        itemView.setLayoutParams(l);

        configureOpenedCard();
    }

    private void openCard() {
        DisplayMetrics displayMetrics = itemView.getContext().getResources().getDisplayMetrics();
//                    Float padding = 15 * displayMetrics.density;
        Float padding = 0F;
        Float paddingTop = 2F * displayMetrics.density;

        GridLayoutManager.LayoutParams l = (GridLayoutManager.LayoutParams) ((CardView) itemView).getLayoutParams();
        l.setMargins(padding.intValue(), paddingTop.intValue(), padding.intValue(), paddingTop.intValue());
        itemView.setLayoutParams(l);

        configureClosedCard();
    }

    private void configureClosedCard() {

        containerAct.setVisibility(View.VISIBLE);
        textValueLabel.setVisibility(View.VISIBLE);
        textCategoryLabel.setVisibility(View.VISIBLE);

        if (!textContent.getText().toString().trim().isEmpty()) {
            textContent.setVisibility(View.VISIBLE);
            textContentLabel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void bind(EntityAbs model) {

        final Transaction transaction = (Transaction) model;
        final NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumFractionDigits(2);

        textValue.setText("R$ " + numberFormat.format(transaction.getValue()));
        textCategory.setText(transaction.getCategory().getName());
        textContent.setText(transaction.getContent());

        configureOpenedCard();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCard(transaction);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCard(transaction);
            }
        });
    }

    private void configureOpenedCard() {

        containerAct.setVisibility(View.GONE);
        textValueLabel.setVisibility(View.GONE);
        textCategoryLabel.setVisibility(View.GONE);
        textContent.setVisibility(View.GONE);
        textContentLabel.setVisibility(View.GONE);
    }
}