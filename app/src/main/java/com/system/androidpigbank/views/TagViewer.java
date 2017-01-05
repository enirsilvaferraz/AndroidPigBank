package com.system.androidpigbank.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.system.androidpigbank.R;
import com.system.architecture.utils.JavaUtils;

/**
 * Created by eferraz on 03/01/17.
 */

public class TagViewer extends LinearLayout {


    public TagViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);

        int pixel = JavaUtils.AndroidUtil.getPixel(getContext(), 10);
        setPadding(pixel, pixel, pixel, pixel);

        bind("Moradia", "Automóvel", "Cuidados Pessoais", "Transporte Automotivo", "Celular", "Aquisição");
    }

    public void bind(String... tags) {

        int pixel4 = JavaUtils.AndroidUtil.getPixel(getContext(), 4);
        int pixel8 = JavaUtils.AndroidUtil.getPixel(getContext(), 8);

        int textCount = 0;
        LinearLayout aux = null;

        for (String tagString : tags) {

            boolean needsAdd = false;

            if (aux == null) {
                aux = getInnerLinear();
                needsAdd = true;
            }

            textCount += tagString.length();
            if (textCount > 30) {
                aux = getInnerLinear();
                textCount = 0;
                needsAdd = true;
            } else if (!needsAdd) {
                needsAdd = false;
            }

            TextView tag = new TextView(getContext());
            tag.setText(tagString);
            tag.setTextColor(getContext().getColor(R.color.material_white));
            tag.setBackgroundColor(getContext().getColor(R.color.accent));
            tag.setPadding(pixel4, pixel8, pixel4, pixel8);
            tag.setGravity(Gravity.CENTER);

            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f);
            params.setMargins(pixel4, pixel8, pixel4, pixel8);
            tag.setLayoutParams(params);

            aux.addView(tag);

            if (needsAdd) {
                addView(aux);
            }
        }
    }

    @NonNull
    private LinearLayout getInnerLinear() {
        LinearLayout aux;
        aux = new LinearLayout(getContext());
        aux.setOrientation(LinearLayout.HORIZONTAL);
        aux.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        aux.setLayoutParams(param);
        //aux.setBackgroundColor(getContext().getColor(R.color.material_gray));
        return aux;
    }
}
