package com.system.androidpigbank.controllers.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.system.androidpigbank.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by eferraz on 25/04/16.
 */
public class CategoryColorsArrayAdapter extends ArrayAdapter<String> {

    LayoutInflater inflater;
    private Activity activity;
    private ArrayList<Colors> data;

    public CategoryColorsArrayAdapter(AppCompatActivity activitySpinner, int textViewResourceId) {
        super(activitySpinner, textViewResourceId, new ArrayList(Arrays.asList(Colors.values())));

        activity = activitySpinner;
        data = new ArrayList(Arrays.asList(Colors.values()));

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        View row = inflater.inflate(R.layout.item_view_holder_category_colors, parent, false);

        Colors colorEnum = data.get(position);

        View color = row.findViewById(R.id.item_view_category_color);
        TextView name = (TextView) row.findViewById(R.id.item_view_category_name);

        if (position == 0) {
            //color.setBackgroundColor(R.colorEnum.material_white);
            name.setText("Please select the colorEnum");
        } else {
            color.setBackgroundColor(colorEnum.getColorId());
            name.setText(colorEnum.name());
        }

        return row;
    }

    private enum Colors {

        RED(R.color.material_red),
        PINK(R.color.material_pink),
        PURPLE(R.color.material_purple),
        DEEP_PURPLE(R.color.material_deep_purple),
        INDIGO(R.color.material_indigo),
        BLUE(R.color.material_blue),
        LIGHT_BLUE(R.color.material_light_blue),
        CYAN(R.color.material_cyan),
        GREEN(R.color.material_green),
        LIGHT_GREEN(R.color.material_light_green),
        LIME(R.color.material_lime),
        YELLOW(R.color.material_yellow),
        AMBER(R.color.material_amber),
        ORANGE(R.color.material_orange),
        DEEP_ORANGE(R.color.material_deep_orange),
        BROWN(R.color.material_brown),
        GRAY(R.color.material_gray),
        BLUE_GRAY(R.color.material_blue_gray),
        BLACK(R.color.material_black),
        WHITE(R.color.material_white);

        private int colorId;

        Colors(int colorId) {
            this.colorId = colorId;
        }

        public int getColorId() {
            return colorId;
        }
    }

}

