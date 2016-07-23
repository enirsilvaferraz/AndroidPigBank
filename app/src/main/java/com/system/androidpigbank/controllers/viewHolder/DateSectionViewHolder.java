package com.system.androidpigbank.controllers.viewHolder;

import android.view.View;
import android.widget.TextView;

import com.system.androidpigbank.R;
import com.system.androidpigbank.architecture.viewHolders.ViewHolderAbs;
import com.system.androidpigbank.controllers.vos.DateSection;
import com.system.androidpigbank.models.entities.EntityAbs;

/**
 * Created by eferraz on 26/12/15.
 */
public class DateSectionViewHolder extends ViewHolderAbs {

    private TextView txtTitle;

    public DateSectionViewHolder(View v) {
        super(v, null, null);

        txtTitle = (TextView) v.findViewById(R.id.item_section_date);
    }

    @Override
    public void bind(EntityAbs model) {
        DateSection section = (DateSection) model;
        txtTitle.setText(section.getTitle());
    }
}
