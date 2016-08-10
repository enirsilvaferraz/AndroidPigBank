package com.system.androidpigbank.controllers.helpers.constant;

import com.system.androidpigbank.R;

public enum Colors {

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
    WHITE(R.color.material_white),

    GRAY_300(R.color.material_gray_300);

    private int colorId;

    Colors(int colorId) {
        this.colorId = colorId;
    }

    public int getColorId() {
        return colorId;
    }
}