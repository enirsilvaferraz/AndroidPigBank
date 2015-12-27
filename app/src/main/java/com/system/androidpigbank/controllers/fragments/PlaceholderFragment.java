package com.system.androidpigbank.controllers.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.adapters.TransactionAdapter;
import com.system.androidpigbank.controllers.managers.LoaderResult;
import com.system.androidpigbank.controllers.managers.TransactionManager;
import com.system.androidpigbank.helpers.Constants;
import com.system.androidpigbank.models.entities.Transaction;

import java.util.List;

public class PlaceholderFragment extends Fragment implements LoaderManager.LoaderCallbacks<LoaderResult<List<Transaction>>> {

    private RecyclerView recyclerView;

    public PlaceholderFragment() {
    }

    public static PlaceholderFragment newInstance(int position) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt("POSITION", position);
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

        recyclerView = (RecyclerView) view.findViewById(R.id.transaction_history_recycleView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new TransactionAdapter((AppCompatActivity) getActivity()));

        final int position = getArguments().getInt("POSITION");

        Bundle args = new Bundle();
        args.putInt("MONTH", position);
        getLoaderManager().initLoader(Constants.LOADER_TRANSACTION_SAVE + position, args, this);
    }

    @Override
    public Loader<LoaderResult<List<Transaction>>> onCreateLoader(int id, Bundle args) {
        return new TransactionManager(getContext()).getTransactionByMonth(args.getInt("MONTH"));
    }

    @Override
    public void onLoadFinished(Loader<LoaderResult<List<Transaction>>> loader, LoaderResult<List<Transaction>> data) {
        if (data.isSuccess()) {
            ((TransactionAdapter) recyclerView.getAdapter()).addItens(data.getData());
        } else {
            Snackbar.make(recyclerView, data.getException().getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<LoaderResult<List<Transaction>>> loader) {
    }
}