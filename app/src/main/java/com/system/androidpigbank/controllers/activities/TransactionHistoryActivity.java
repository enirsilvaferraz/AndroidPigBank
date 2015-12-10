package com.system.androidpigbank.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.adapters.TransactionAdapter;
import com.system.androidpigbank.controllers.managers.LoaderResult;
import com.system.androidpigbank.controllers.managers.TransactionManager;
import com.system.androidpigbank.helpers.Constants;
import com.system.androidpigbank.models.entities.Transaction;

import java.util.List;

public class TransactionHistoryActivity extends BaseActivity<List<Transaction>> {

    private RecyclerView recyclerView;
    private View container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        container = findViewById(R.id.transaction_history_container);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TransactionHistoryActivity.this, TransactionManagerActivity.class));
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.transaction_history_recycleView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new TransactionAdapter());

        getSupportLoaderManager().initLoader(Constants.LOADER_TRANSACTION, null, this);
    }

    @Override
    public Loader<LoaderResult<List<Transaction>>> onCreateLoader(int id, Bundle args) {
        return new TransactionManager(this).getCurrentMonth();
    }

    @Override
    public void onLoadFinished(Loader<LoaderResult<List<Transaction>>> loader, LoaderResult<List<Transaction>> data) {
        if (data.isSuccess()) {
            ((TransactionAdapter) recyclerView.getAdapter()).addItens(data.getData());
        } else {
            Snackbar.make(container, data.getException().getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<LoaderResult<List<Transaction>>> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_transaction_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.transaction_history_act_detail:
                startActivity(new Intent(this, TransactionDetailActivity.class));
                return true;
        }

        return false;
    }
}
