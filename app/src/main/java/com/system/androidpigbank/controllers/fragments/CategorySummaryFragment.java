package com.system.androidpigbank.controllers.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.adapters.recyclerv.CategorySummaryAdapter;
import com.system.androidpigbank.models.sqlite.entities.Category;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategorySummaryFragment extends Fragment {

    @BindView(R.id.category_recyclerview)
    RecyclerView recyclerview;

    private List<Category> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_summary, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final CategorySummaryAdapter adapter = new CategorySummaryAdapter();
        adapter.addItens(getData());

        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        recyclerview.setAdapter(adapter);
    }

    public static CategorySummaryFragment newInstance() {
        CategorySummaryFragment fragment = new CategorySummaryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void setData(List<Category> data) {
        this.data = data;
    }

    public List<Category> getData() {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data;
    }
}
