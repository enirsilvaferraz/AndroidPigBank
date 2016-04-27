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
import com.system.androidpigbank.helpers.constants.Colors;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by eferraz on 25/04/16.
 */
public class CategoryColorsArrayAdapter extends ArrayAdapter<Colors> {

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
            name.setText("Please select the color");
        } else {
            color.setBackgroundResource(colorEnum.getColorId());

            int resourceId = activity.getResources().getIdentifier(colorEnum.getClass().getName() + "." + colorEnum.name(), "string", activity.getPackageName());
            name.setText(activity.getString(resourceId));
        }

        return row;
    }
}

