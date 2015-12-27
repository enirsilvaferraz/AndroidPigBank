package com.system.androidpigbank.controllers.vIewHolders;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.system.androidpigbank.models.entities.EntityAbs;

/**
 * Created by eferraz on 05/12/15.
 */
public abstract class ViewHolderAbs extends RecyclerView.ViewHolder {

    private AppCompatActivity activity;
    private RecyclerView.Adapter adapter;

    public ViewHolderAbs(View itemView, AppCompatActivity activity, RecyclerView.Adapter adapter) {
        super(itemView);
        this.activity = activity;
        this.adapter = adapter;
    }

    public abstract void bind(EntityAbs model);

    public AppCompatActivity getActivity() {
        return activity;
    }

    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }
}
