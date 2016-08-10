package com.system.androidpigbank.controllers.behaviors;

import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

/**
 * Created by eferraz on 10/08/16.
 */

public final class HighlightCardBehavior {

    public static void turnOn(CardView cardView) {
        GridLayoutManager.LayoutParams param = (GridLayoutManager.LayoutParams) cardView.getLayoutParams();
        param.setMargins(30, 30, 30, 30);
        cardView.setLayoutParams(param);
    }

    public static void turnOff(CardView cardView) {
        GridLayoutManager.LayoutParams param = (GridLayoutManager.LayoutParams) cardView.getLayoutParams();
        param.setMargins(0, 0, 0, 0);
        cardView.setLayoutParams(param);
    }

    public static void bind(boolean expanded, CardView cardView) {

        GridLayoutManager.LayoutParams param = (GridLayoutManager.LayoutParams) cardView.getLayoutParams();
        if (expanded) {
            param.setMargins(30, 30, 30, 30);
        } else {
            param.setMargins(0, 0, 0, 0);
        }
        cardView.setLayoutParams(param);
    }
}
