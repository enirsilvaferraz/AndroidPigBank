package com.system.androidpigbank.controllers.vIewHolders;

import android.view.View;
import android.widget.TextView;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.vos.TotalFooter;
import com.system.androidpigbank.models.entities.EntityAbs;

import java.text.NumberFormat;

/**
 * Created by eferraz on 03/01/16.
 */
public class FooterViewHolder extends ViewHolderAbs {

    private TextView total;

    public FooterViewHolder(final View v) {
        super(v, null, null);

        total = (TextView) v.findViewById(R.id.item_footer_total);
    }

    @Override
    public void bind(final EntityAbs model) {

        TotalFooter totalFooter = (TotalFooter) model;

        final NumberFormat instance = NumberFormat.getInstance();
        instance.setMinimumFractionDigits(2);
        instance.setMinimumIntegerDigits(1);

        total.setText(instance.format(totalFooter.getTotal()));
    }
}
