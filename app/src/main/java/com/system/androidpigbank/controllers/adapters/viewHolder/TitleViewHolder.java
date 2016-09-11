package com.system.androidpigbank.controllers.adapters.viewHolder;

import android.view.View;
import android.widget.TextView;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.vos.TitleVO;
import com.system.architecture.adapters.CardAdapter;
import com.system.architecture.adapters.CardViewHolder;

/**
 * Created by eferraz on 03/01/16.
 */
public class TitleViewHolder extends CardViewHolder {

    private TextView tvTitle;

    public TitleViewHolder(final View v, boolean isCardMode) {
        super(v, isCardMode);
        tvTitle = (TextView) v.findViewById(R.id.item_title);
    }

    @Override
    public void bind(CardAdapter.CardModel model, OnClickListener onClickListener) {
        super.bind(model, onClickListener);

        TitleVO category = (TitleVO) model;
        tvTitle.setText(category.getTitle());
    }
}
