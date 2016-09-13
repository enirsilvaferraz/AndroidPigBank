package com.system.architecture.adapters;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.system.architecture.utils.JavaUtils;

/**
 *
 */
public abstract class CardViewHolder extends RecyclerView.ViewHolder {

    public CardViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(CardAdapter.CardModel model, OnClickListener onClickListener) {
        int pixelHorizontal = JavaUtils.AndroidUtil.getPixel(itemView.getResources(), 12);
        GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) itemView.getLayoutParams();
        params.setMargins(pixelHorizontal, 0, pixelHorizontal, 0);
        itemView.setLayoutParams(params);
    }

    public interface OnClickListener {
        void onContainerClicked(int action, CardAdapter.CardModel model);
    }
}