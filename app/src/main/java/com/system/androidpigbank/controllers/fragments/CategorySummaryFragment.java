package com.system.androidpigbank.controllers.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.helpers.IntentRouter;
import com.system.androidpigbank.models.sqlite.business.CategoryBusiness;
import com.system.androidpigbank.models.sqlite.entities.Category;
import com.system.androidpigbank.models.sqlite.entities.Transaction;
import com.system.architecture.adapters.CardAdapter;
import com.system.architecture.adapters.CardViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategorySummaryFragment extends Fragment {

    @BindView(R.id.category_recyclerview)
    RecyclerView recyclerview;

    private List<Category> data;

    public static CategorySummaryFragment newInstance() {
        CategorySummaryFragment fragment = new CategorySummaryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_summary, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final CardAdapter adapter = new CardAdapter(getActivity(), true, new CardViewHolder.OnClickListener() {
            @Override
            public void onContainerClicked(CardAdapter.CardModel model) {
                if (model instanceof Transaction) {
                    IntentRouter.hideDialog();
                    IntentRouter.startTransactionManager((AppCompatActivity) getActivity(), (Transaction) model);
                }
            }
        });
        adapter.addItens(new CategoryBusiness(getContext()).organizeCategorySummaryList(getData()));

        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        recyclerview.setAdapter(adapter);
    }

    public List<Category> getData() {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data;
    }

    public void setData(List<Category> data) {
        this.data = data;
    }
}
