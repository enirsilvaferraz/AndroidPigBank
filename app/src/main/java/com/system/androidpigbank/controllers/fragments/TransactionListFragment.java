package com.system.androidpigbank.controllers.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.adapters.recyclerv.TransactionAdapter;
import com.system.androidpigbank.models.entities.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionListFragment extends Fragment {

    private static final int SPAN_COUNT = 1;
    private RecyclerView recyclerView;
    private List<Transaction> data;

    public TransactionListFragment() {
    }

    public static TransactionListFragment newInstance() {
        TransactionListFragment fragment = new TransactionListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transaction_history_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TransactionAdapter adapter = new TransactionAdapter((AppCompatActivity) getActivity());
        adapter.addItens(getData());

        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT, GridLayoutManager.VERTICAL, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.transaction_history_recycleView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void setData(List<Transaction> data) {
        this.data = data;
    }

    public List<Transaction> getData() {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data;
    }
}