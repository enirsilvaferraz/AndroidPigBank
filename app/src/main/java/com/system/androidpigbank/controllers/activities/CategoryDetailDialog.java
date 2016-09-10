package com.system.androidpigbank.controllers.activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.adapters.recyclerv.TransactionAdapter;
import com.system.androidpigbank.controllers.helpers.constant.Constants;
import com.system.androidpigbank.controllers.vos.TotalVO;
import com.system.androidpigbank.models.sqlite.entities.Category;
import com.system.androidpigbank.models.sqlite.entities.Transaction;
import com.system.androidpigbank.views.CategorySummaryView;
import com.system.architecture.adapters.CardAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Enir on 10/09/2016.
 */

public class CategoryDetailDialog extends DialogFragment {

    @BindView(R.id.category_detail_header)
    CategorySummaryView header;

    @BindView(R.id.category_detail_recycler)
    RecyclerView recyclerView;

    public static CategoryDetailDialog newInstance(Category category) {
        CategoryDetailDialog frag = new CategoryDetailDialog();
        Bundle args = new Bundle();
        args.putParcelable(Constants.BUNDLE_MODEL_DEFAULT, category);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_category_detail, container);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        final Parcelable parcelable = getArguments().getParcelable(Constants.BUNDLE_MODEL_DEFAULT);
        if (getArguments() != null && parcelable != null) {

            Category category = (Category) parcelable;
            header.bind(category);

            final CardAdapter adapter = new CardAdapter((getActivity()));

            String nomeCat = null;
            Double value = 0D;

            List<CardAdapter.CardModel> list = new ArrayList<>();
            for (int i = 0; i < category.getTransactionList().size(); i++) {

                Transaction transaction = category.getTransactionList().get(i);
                String name = transaction.getCategorySecondary() != null ? transaction.getCategorySecondary().getName() : "";

                if (!list.isEmpty() && !name.equals(nomeCat)) {
                    list.add(new TotalVO(value));
                    value = 0D;
                }

                nomeCat = name;

                list.add(transaction);
                value += transaction.getValue();


                if (i == category.getTransactionList().size() - 1){
                    list.add(new TotalVO(value));
                }
            }
            adapter.addItens(list);

            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(adapter);
        }
    }

}
