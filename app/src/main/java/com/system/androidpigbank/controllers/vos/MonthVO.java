package com.system.androidpigbank.controllers.vos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.system.androidpigbank.models.firebase.serializers.GsonUtil;
import com.system.architecture.utils.DTOAbs;
import com.system.androidpigbank.models.firebase.dtos.MonthDTO;
import com.system.architecture.adapters.CardAdapter;
import com.system.architecture.managers.EntityAbs;
import com.system.architecture.utils.JavaUtils;

/**
 * Created by Enir on 22/07/2016.
 */
public class MonthVO extends EntityAbs implements VOIf, Parcelable, CardAdapter.CardModel {

    public static final Creator<MonthVO> CREATOR = new Creator<MonthVO>() {
        @Override
        public MonthVO createFromParcel(Parcel source) {
            return new MonthVO(source);
        }

        @Override
        public MonthVO[] newArray(int size) {
            return new MonthVO[size];
        }
    };
    @Expose
    private String key;
    @Expose
    private Integer month;
    @Expose
    private Integer year;
    @Expose
    private Double value;

    public MonthVO(Integer month, Integer year, Double value) {
        this.month = month;
        this.year = year;
        this.value = value;
    }

    public MonthVO() {
    }

    protected MonthVO(Parcel in) {
        this.key = in.readString();
        this.month = (Integer) in.readValue(Integer.class.getClassLoader());
        this.year = (Integer) in.readValue(Integer.class.getClassLoader());
        this.value = (Double) in.readValue(Double.class.getClassLoader());
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MonthVO monthVO = (MonthVO) o;

        if (month != null ? !month.equals(monthVO.month) : monthVO.month != null) return false;
        return year != null ? year.equals(monthVO.year) : monthVO.year == null;

    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int hashCode() {
        int result = month != null ? month.hashCode() : 0;
        result = 31 * result + (year != null ? year.hashCode() : 0);
        return result;
    }

    @Override
    public DTOAbs toDTO() {
        return GsonUtil.getInstance().fromMonth().toDTO(this, MonthDTO.class);
    }

    @Override
    public CardAdapter.CardViewType getViewType() {
        return CardAdapter.CardViewType.CARD_MONTH;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeValue(this.month);
        dest.writeValue(this.year);
        dest.writeValue(this.value);
    }
}
