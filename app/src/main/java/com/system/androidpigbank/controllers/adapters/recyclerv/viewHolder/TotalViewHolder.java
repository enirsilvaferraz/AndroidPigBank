package com.system.androidpigbank.controllers.adapters.recyclerv.viewHolder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.vos.TotalVO;
import com.system.architecture.activities.CardAdapterAbs;
import com.system.architecture.activities.CardViewHolder;
import com.system.architecture.utils.JavaUtils;

/**
 * Created by eferraz on 03/01/16.
 */
public class TotalViewHolder extends CardViewHolder {

    private TextView tvValueStart;
    private TextView tvValueEnd;

    public TotalViewHolder(final View v) {
        super(v);
        tvValueStart = (TextView) v.findViewById(R.id.item_footer_value_start);
        tvValueEnd = (TextView) v.findViewById(R.id.item_footer_value_end);
    }

    @Override
    public void bind(CardAdapterAbs.CardModel model, final OnClickListener onClickListener) {
        super.bind(model, onClickListener);

        TotalVO totalFooter = (TotalVO) model;

        if (!TextUtils.isEmpty(totalFooter.getValueStart())) {
            tvValueStart.setText(totalFooter.getValueStart());
        }

        if (!TextUtils.isEmpty(totalFooter.getValueEnd())) {
            tvValueEnd.setText(totalFooter.getValueEnd());
        }
    }
}
