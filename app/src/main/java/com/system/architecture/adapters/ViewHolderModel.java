package com.system.architecture.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 *
 */
public abstract class ViewHolderModel extends RecyclerView.ViewHolder {

    public ViewHolderModel(View itemView) {
        super(itemView);
    }

    public abstract void bind(CardAdapter.CardModel model, OnClickListener onClickListener);

    public interface OnClickListener {
        void onContainerClicked (CardAdapter.CardModel model);
    }
}