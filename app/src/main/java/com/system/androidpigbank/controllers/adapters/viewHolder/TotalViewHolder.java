package com.system.androidpigbank.controllers.adapters.viewHolder;

import android.view.View;
import android.widget.TextView;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.vos.TotalVO;
import com.system.architecture.adapters.CardAdapter;
import com.system.architecture.adapters.CardViewHolder;
import com.system.architecture.utils.JavaUtils;

/**
 * Created by eferraz on 03/01/16.
 */
public class TotalViewHolder extends CardViewHolder {

    private TextView tvValueStart;
    private TextView tvValueEnd;

    public TotalViewHolder(final View v, boolean isCardMode) {
        super(v, isCardMode);
        tvValueStart = (TextView) v.findViewById(R.id.item_footer_value_start);
        tvValueEnd = (TextView) v.findViewById(R.id.item_footer_value_end);
    }

    @Override
    public void bind(CardAdapter.CardModel model, final OnClickListener onClickListener) {
        super.bind(model, onClickListener);

        TotalVO totalFooter = (TotalVO) model;

        if (totalFooter.getValueStart() != null && totalFooter.getValueStart() != 0D) {
            tvValueStart.setText(JavaUtils.NumberUtil.currencyFormat(totalFooter.getValueStart()));
        }

        if (totalFooter.getValueEnd() != null && totalFooter.getValueEnd() != 0D) {
            tvValueEnd.setText(JavaUtils.NumberUtil.currencyFormat(totalFooter.getValueEnd()));
        }
    }
}
