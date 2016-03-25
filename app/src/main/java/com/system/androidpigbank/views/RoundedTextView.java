package com.system.androidpigbank.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.system.androidpigbank.R;

/**
 * Created by eferraz on 03/01/16.
 */
public class RoundedTextView extends LinearLayout {
    
    private final TextView textView;

    public RoundedTextView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        setBackgroundResource(R.drawable.drawable_rounded);
        setGravity(Gravity.CENTER);

        textView = new TextView(context);
        textView.setTextSize(20);

        addView(textView);

    }

    public void setTextView(final String text) {
        int size = text.length() > 2 ? 2 : text.length();
        textView.setText(text.substring(0, size));
    }
}
