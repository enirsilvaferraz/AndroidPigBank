package com.system.androidpigbank.controllers.vos;

import android.os.Parcel;
import android.os.Parcelable;

import com.system.androidpigbank.models.firebase.dtos.DTOAbs;
import com.system.androidpigbank.models.firebase.dtos.MonthDTO;
import com.system.architecture.managers.EntityAbs;
import com.system.architecture.utils.JavaUtils;

/**
 * Created by Enir on 22/07/2016.
 */
public class MonthVO extends EntityAbs implements VOIf, Parcelable {

    private Integer month;

    private Integer year;

    private Double value;

    public MonthVO(Integer month, Integer year, Double value) {
        this.month = month;
        this.year = year;
        this.value = value;
    }

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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.month);
        dest.writeValue(this.year);
        dest.writeValue(this.value);
    }

    public MonthVO() {
    }

    protected MonthVO(Parcel in) {
        this.month = (Integer) in.readValue(Integer.class.getClassLoader());
        this.year = (Integer) in.readValue(Integer.class.getClassLoader());
        this.value = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<MonthVO> CREATOR = new Parcelable.Creator<MonthVO>() {
        @Override
        public MonthVO createFromParcel(Parcel source) {
            return new MonthVO(source);
        }

        @Override
        public MonthVO[] newArray(int size) {
            return new MonthVO[size];
        }
    };

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public DTOAbs toDTO() {
        return JavaUtils.GsonUtil.getInstance().fromCategory().toDTO(this, MonthDTO.class);
    }
}
