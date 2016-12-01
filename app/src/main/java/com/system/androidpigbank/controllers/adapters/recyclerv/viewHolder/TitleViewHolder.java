package com.system.androidpigbank.controllers.adapters.recyclerv.viewHolder;

import android.view.View;
import android.widget.TextView;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.vos.TitleVO;
import com.system.architecture.activities.CardAdapterAbs;
import com.system.architecture.activities.CardViewHolder;

/**
 * Created by eferraz on 03/01/16.
 */
public class TitleViewHolder extends CardViewHolder {

    private TextView tvTitle;

    public TitleViewHolder(final View v) {
        super(v);
        tvTitle = (TextView) v.findViewById(R.id.item_title);
    }

    @Override
    public void bind(CardAdapterAbs.CardModel model, OnClickListener onClickListener) {
        super.bind(model, onClickListener);

        TitleVO category = (TitleVO) model;
        tvTitle.setText(category.getTitle());
    }
}
