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
import com.system.androidpigbank.controllers.adapters.recyclerv.MonthAdapter;
import com.system.androidpigbank.controllers.vos.Month;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Enir on 22/07/2016.
 */
public class MonthFragment extends Fragment {

    private MonthAdapter.OnItemClicked onItemClicked;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private List<Month> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mouth_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MonthAdapter adapter = new MonthAdapter();
        adapter.addItens(getData());

        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        recyclerview.setAdapter(adapter);
        ((MonthAdapter) recyclerview.getAdapter()).setOnItemClicked(onItemClicked);
    }

    public static MonthFragment newInstance(MonthAdapter.OnItemClicked onItemClicked) {
        MonthFragment fragment = new MonthFragment();
        fragment.setOnItemClicked(onItemClicked);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnItemClicked(MonthAdapter.OnItemClicked onItemClicked) {
        this.onItemClicked = onItemClicked;
    }

    public void setData(List<Month> data) {
        this.data = data;
    }

    public List<Month> getData() {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data;
    }
}
