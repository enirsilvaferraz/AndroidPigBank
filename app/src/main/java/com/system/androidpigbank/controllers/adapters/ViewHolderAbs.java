package com.system.androidpigbank.controllers.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.system.androidpigbank.models.entities.EntityAbs;

/**
 * Created by eferraz on 05/12/15.
 */
public abstract class ViewHolderAbs extends RecyclerView.ViewHolder {

    public ViewHolderAbs(View itemView) {
        super(itemView);
    }

    public abstract void bind(EntityAbs model);
}
