package com.system.androidpigbank.models.firebase.dtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.system.androidpigbank.controllers.vos.EstimateVO;
import com.system.androidpigbank.models.firebase.serializers.GsonUtil;
import com.system.architecture.models.DTOAbs;
import com.system.architecture.models.VOAbs;

/**
 * Created by eferraz on 05/12/15.
 */
public class EstimateDTO extends DTOAbs {

    @SerializedName("key")
    @Expose
    private String key;

    @SerializedName("category")
    @Expose
    private String category;

    @SerializedName("categorySecondary")
    @Expose
    private String categorySecondary;

    @SerializedName("day")
    @Expose
    private String day;

    @SerializedName("plannedValue")
    @Expose
    private String plannedValue;

    @SerializedName("quinzena")
    @Expose
    private String quinzena;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategorySecondary() {
        return categorySecondary;
    }

    public void setCategorySecondary(String categorySecondary) {
        this.categorySecondary = categorySecondary;

    }

    public String getQuinzena() {
        return quinzena;
    }

    public void setQuinzena(String quinzena) {
        this.quinzena = quinzena;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getPlannedValue() {
        return plannedValue;
    }

    public void setPlannedValue(String plannedValue) {
        this.plannedValue = plannedValue;
    }

    @Override
    public VOAbs toEntity() {
        return GsonUtil.getInstance().fromCategory().toEntity(this, EstimateVO.class);
    }
}