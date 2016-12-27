package com.system.architecture.activities;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.system.architecture.R;

/**
 *
 */
public abstract class CardViewHolder extends RecyclerView.ViewHolder {

    public int ACTION_VIEW = 1;
    public int ACTION_EDIT = 2;
    public int ACTION_DELETE = 3;
    public int ACTION_COPY = 4;

    public CardViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(final CardAdapterAbs.CardModel model, final OnClickListener onClickListener) {
        Float pixelHorizontal = itemView.getResources().getDimension(R.dimen.margin_card);
        GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) itemView.getLayoutParams();
        params.setMargins(pixelHorizontal.intValue(), 0, pixelHorizontal.intValue(), 0);
        itemView.setLayoutParams(params);

        if (onClickListener != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onContainerClicked(ACTION_VIEW, model);
                }
            });
        }
    }

    public interface OnClickListener {
        void onContainerClicked(int action, CardAdapterAbs.CardModel model);
    }
}