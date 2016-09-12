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
public class WhiteSpaceViewHolder extends CardViewHolder {

    public WhiteSpaceViewHolder(final View v, boolean isCardMode) {
        super(v, isCardMode);
    }

    @Override
    public void bind(CardAdapter.CardModel model, OnClickListener onClickListener) {
        super.bind(model, onClickListener);
    }
}
