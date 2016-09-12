package com.system.architecture.adapters;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.system.architecture.utils.JavaUtils;

/**
 *
 */
public abstract class CardViewHolder extends RecyclerView.ViewHolder {

    protected boolean isCardMode;

    public CardViewHolder(View itemView, boolean isCardMode) {
        super(itemView);
        this.isCardMode = isCardMode;
    }

    public void bind(CardAdapter.CardModel model, OnClickListener onClickListener) {

        if (isCardMode) {
            int pixelVertical = JavaUtils.AndroidUtil.getPixel(itemView.getResources(), 6);
            int pixelHorizontal = JavaUtils.AndroidUtil.getPixel(itemView.getResources(), 12);

            GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) itemView.getLayoutParams();

            // TODO MARRETA
            if (model.getCardStrategy() == null){
                model.setCardStrategy(CardAdapter.CardModeItem.MIDDLE);
            }

            switch (model.getCardStrategy()) {
                case SINGLE:
                    params.setMargins(pixelHorizontal, pixelVertical, pixelHorizontal, pixelVertical);
                    break;
                case START:
                    params.setMargins(pixelHorizontal, pixelVertical, pixelHorizontal, 0);
                    break;
                case MIDDLE:
                    params.setMargins(pixelHorizontal, 0, pixelHorizontal, 0);
                    break;
                case END:
                    params.setMargins(pixelHorizontal, 0, pixelHorizontal, pixelVertical);
                    break;
                case NO_STRATEGY:
                    break;
            }

            itemView.setLayoutParams(params);
        }
    }

    public interface OnClickListener {
        void onContainerClicked(int action, CardAdapter.CardModel model);
    }
}