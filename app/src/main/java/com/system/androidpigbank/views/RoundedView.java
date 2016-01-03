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
public class RoundedView extends LinearLayout {
    
    private final TextView textView;

    public RoundedView(final Context context, AttributeSet attrs) {
        super(context, attrs);

//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View v = inflater.inflate(R.layout.component_rounded, this, true);
//        this.textView = (TextView) v.findViewById(R.id.component_rounded_textview);

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
