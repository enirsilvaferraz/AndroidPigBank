package com.system.androidpigbank.controllers.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.system.androidpigbank.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eferraz on 03/01/17.
 */

public class TransactionManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_content_transaction_manager_v3);
        ButterKnife.bind(this);


    }
}
