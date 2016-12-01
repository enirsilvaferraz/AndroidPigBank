package com.system.androidpigbank.views;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.system.androidpigbank.R;
import com.system.architecture.models.VOAbs;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eferraz on 22/08/16.
 */

public class CardActionBarView extends LinearLayout {

    @BindView(R.id.custom_bar_edit_action)
    ImageButton btEditAction;

    @BindView(R.id.custom_bar_delete_action)
    ImageButton btDeleteAction;

    @BindView(R.id.custom_bar_copy_action)
    ImageButton btCopyAction;

    public CardActionBarView(Context context) {
        super(context);
        inflate(getContext(), R.layout.item_view_holder_card_bar, this);
        ButterKnife.bind(this);
    }

    public CardActionBarView bind(final VOAbs entityAbs, final OnClickListener onClickListener) {

        if (onClickListener != null) {

            btEditAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onEditClicked(entityAbs);
                }
            });

            btDeleteAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onDeleteClicked(entityAbs);
                }
            });

            btCopyAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onCopyClicked(entityAbs);
                }
            });
        }

        return this;
    }

    public interface OnClickListener {
        void onEditClicked(VOAbs model);

        void onDeleteClicked(VOAbs model);

        void onCopyClicked(VOAbs model);
    }
}
