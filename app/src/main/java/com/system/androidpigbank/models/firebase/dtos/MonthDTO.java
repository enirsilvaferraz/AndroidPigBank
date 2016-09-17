package com.system.androidpigbank.models.firebase.dtos;

import com.google.gson.annotations.Expose;
import com.system.androidpigbank.controllers.vos.MonthVO;
import com.system.architecture.managers.EntityAbs;
import com.system.architecture.utils.JavaUtils;

/**
 * Created by Enir on 22/07/2016.
 */
public class MonthDTO extends DTOAbs {

    @Expose
    private Integer month;

    @Expose
    private Integer year;

    @Expose
    private Double value;

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public EntityAbs toEntity() {
        return JavaUtils.GsonUtil.getInstance().fromMonth().toEntity(this, MonthVO.class);
    }
}
