package com.system.androidpigbank.controllers.adapters.viewHolder;

import android.view.View;
import android.widget.TextView;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.vos.TotalVO;
import com.system.architecture.adapters.CardAdapter;
import com.system.architecture.adapters.CardViewHolder;

import java.text.NumberFormat;

/**
 * Created by eferraz on 03/01/16.
 */
public class TotalViewHolder extends CardViewHolder {

    private TextView total;

    public TotalViewHolder(final View v, boolean isCardMode) {
        super(v, isCardMode);
        total = (TextView) v.findViewById(R.id.item_footer_total);
    }

    @Override
    public void bind(CardAdapter.CardModel model, final OnClickListener onClickListener) {
        super.bind(model, onClickListener);

        TotalVO totalFooter = (TotalVO) model;

        final NumberFormat instance = NumberFormat.getInstance();
        instance.setMinimumFractionDigits(2);
        instance.setMinimumIntegerDigits(1);

        total.setText(itemView.getContext().getString(R.string.item_transaction_footer_value, instance.format(totalFooter.getTotal())));
    }
}
