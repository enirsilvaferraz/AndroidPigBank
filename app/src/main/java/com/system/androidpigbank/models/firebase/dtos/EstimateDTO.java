package com.system.androidpigbank.models.firebase.dtos;

import com.google.gson.annotations.Expose;
import com.system.androidpigbank.controllers.vos.EstimateVO;
import com.system.androidpigbank.models.firebase.serializers.GsonUtil;
import com.system.architecture.models.DTOAbs;
import com.system.architecture.models.VOAbs;

/**
 * Created by eferraz on 05/12/15.
 */
public class EstimateDTO extends DTOAbs {

    @Expose
    private String key;
    @Expose
    private String category;
    @Expose
    private String categorySecondary;
    @Expose
    private String day;
    @Expose
    private String plannedValue;
    @Expose
    private String spentValue;
    @Expose
    private String savedValue;
    @Expose
    private String percentSpentValue;

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

    public String getSpentValue() {
        return spentValue;
    }

    public void setSpentValue(String spentValue) {
        this.spentValue = spentValue;
    }

    public String getSavedValue() {
        return savedValue;
    }

    public void setSavedValue(String savedValue) {
        this.savedValue = savedValue;
    }

    public String getPercentSpentValue() {
        return percentSpentValue;
    }

    public void setPercentSpentValue(String percentSpentValue) {
        this.percentSpentValue = percentSpentValue;
    }

    @Override
    public VOAbs toEntity() {
        return GsonUtil.getInstance().fromCategory().toEntity(this, EstimateVO.class);
    }
}