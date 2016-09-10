package com.system.androidpigbank.controllers.adapters.viewHolder;

import android.view.View;
import android.widget.TextView;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.vos.TotalFooter;
import com.system.androidpigbank.controllers.vos.TotalVO;
import com.system.androidpigbank.models.sqlite.entities.EntityAbs;
import com.system.architecture.adapters.CardAdapter;
import com.system.architecture.adapters.ViewHolderModel;
import com.system.architecture.viewHolders.ViewHolderAbs;

import java.text.NumberFormat;

/**
 * Created by eferraz on 03/01/16.
 */
public class TotalViewHolder extends ViewHolderModel {

    private TextView total;

    public TotalViewHolder(final View v) {
        super(v);
        total = (TextView) v.findViewById(R.id.item_footer_total);
    }

    @Override
    public void bind(CardAdapter.CardModel model, final OnClickListener onClickListener) {
        TotalVO totalFooter = (TotalVO) model;

        final NumberFormat instance = NumberFormat.getInstance();
        instance.setMinimumFractionDigits(2);
        instance.setMinimumIntegerDigits(1);

        total.setText(itemView.getContext().getString(R.string.item_transaction_footer_value, instance.format(totalFooter.getTotal())));
    }
}
