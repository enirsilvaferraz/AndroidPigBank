package com.system.androidpigbank.controllers.adapters.recyclerv.viewHolder;

import android.view.View;
import android.widget.ImageButton;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.helpers.Constants;
import com.system.androidpigbank.controllers.vos.ActionBarVO;
import com.system.architecture.activities.CardAdapterAbs;
import com.system.architecture.activities.CardViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Enir on 11/09/2016.
 */

public class ActionBarViewHolder extends CardViewHolder {

    @BindView(R.id.custom_bar_edit_action)
    ImageButton btEditAction;

    @BindView(R.id.custom_bar_delete_action)
    ImageButton btDeleteAction;

    @BindView(R.id.custom_bar_copy_action)
    ImageButton btCopyAction;

    public ActionBarViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(final CardAdapterAbs.CardModel model, final OnClickListener onClickListener) {
        super.bind(model, onClickListener);

        final ActionBarVO vo = (ActionBarVO) model;

        if (vo.getActionsToHide() != null) {

            boolean contains = vo.getActionsToHide().contains(ActionBarVO.Actions.COPY);
            btCopyAction.setVisibility(contains ? View.GONE : View.VISIBLE);

            contains = vo.getActionsToHide().contains(ActionBarVO.Actions.EDIT);
            btEditAction.setVisibility(contains ? View.GONE : View.VISIBLE);

            contains = vo.getActionsToHide().contains(ActionBarVO.Actions.DELETE);
            btDeleteAction.setVisibility(contains ? View.GONE : View.VISIBLE);
        }

        if (onClickListener != null) {

            btEditAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onContainerClicked(Constants.ACTION_EDIT, vo.getCardReferency());
                }
            });

            btDeleteAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onContainerClicked(Constants.ACTION_DELETE, vo.getCardReferency());
                }
            });

            btCopyAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onContainerClicked(Constants.ACTION_COPY, vo.getCardReferency());
                }
            });
        }
    }
}
