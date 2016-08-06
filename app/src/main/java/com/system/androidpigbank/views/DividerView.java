package com.system.androidpigbank.views;

import android.content.Context;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.system.androidpigbank.R;

/**
 * Created by eferraz on 06/08/16.
 */

public class DividerView extends LinearLayout {

    public DividerView(Context context) {
        super(context);

        Float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, px.intValue()));
        setBackgroundResource(R.color.divider);
    }
}
