package com.system.androidpigbank.controllers.adapters.recyclerv.viewHolder;

import android.view.View;

import com.system.architecture.activities.CardAdapterAbs;
import com.system.architecture.activities.CardViewHolder;

/**
 * Created by eferraz on 03/01/16.
 */
public class WhiteSpaceViewHolder extends CardViewHolder {

    public WhiteSpaceViewHolder(final View v) {
        super(v);
    }

    @Override
    public void bind(CardAdapterAbs.CardModel model, OnClickListener onClickListener) {
        super.bind(model, onClickListener);
    }
}
