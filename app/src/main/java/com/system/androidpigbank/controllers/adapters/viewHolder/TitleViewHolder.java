package com.system.androidpigbank.controllers.adapters.viewHolder;

import android.view.View;
import android.widget.TextView;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.vos.TitleVO;
import com.system.androidpigbank.controllers.vos.TotalFooter;
import com.system.androidpigbank.models.entities.Category;
import com.system.androidpigbank.models.entities.EntityAbs;
import com.system.architecture.viewHolders.ViewHolderAbs;

import java.text.NumberFormat;

/**
 * Created by eferraz on 03/01/16.
 */
public class TitleViewHolder extends ViewHolderAbs {

    private TextView tvTitle;

    public TitleViewHolder(final View v) {
        super(v, null, null);
        tvTitle = (TextView) v.findViewById(R.id.item_title);
    }

    @Override
    public void bind(final EntityAbs model) {
        TitleVO category = (TitleVO) model;
        tvTitle.setText(category.getTitle());
    }
}
