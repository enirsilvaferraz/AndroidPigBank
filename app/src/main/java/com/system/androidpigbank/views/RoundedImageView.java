package com.system.androidpigbank.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.system.androidpigbank.R;

/**
 * Created by eferraz on 03/01/16.
 */
public class RoundedImageView extends LinearLayout {

    private final ImageView imageView;

    public RoundedImageView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        setBackgroundResource(R.drawable.drawable_rounded);
        setGravity(Gravity.CENTER);

        imageView = new ImageView(context);
        final ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = convertDpToPx(context, 24);
        layoutParams.height = convertDpToPx(context, 24);
        imageView.setLayoutParams(layoutParams);

        addView(imageView);

    }

    private int convertDpToPx(Context context, int valueDP) {
        return Float.valueOf(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueDP, context.getResources().getDisplayMetrics())).intValue();
    }

    public void setImageView(int resourceId) {
        imageView.setImageResource(resourceId);
    }
}
