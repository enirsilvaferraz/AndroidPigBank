package com.system.androidpigbank.models.firebase.dtos;

import com.system.androidpigbank.controllers.vos.Month;
import com.system.androidpigbank.models.sqlite.entities.Transaction;
import com.system.architecture.managers.EntityAbs;
import com.system.architecture.utils.JavaUtils;

/**
 * Created by Enir on 22/07/2016.
 */
public class MonthDTO extends DTOAbs {

    private Integer month;

    private Integer year;

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
        return JavaUtils.GsonUtil.getInstance().fromMonth().toEntity(this, Month.class);
    }
}
