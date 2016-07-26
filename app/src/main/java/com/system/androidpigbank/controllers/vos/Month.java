package com.system.androidpigbank.controllers.vos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Enir on 22/07/2016.
 */
public class Month implements Parcelable {

    private Integer month;

    private Integer year;

    private Double value;

    public Month(Integer month, Integer year, Double value) {
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

    public Month() {
    }

    protected Month(Parcel in) {
        this.month = (Integer) in.readValue(Integer.class.getClassLoader());
        this.year = (Integer) in.readValue(Integer.class.getClassLoader());
        this.value = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<Month> CREATOR = new Parcelable.Creator<Month>() {
        @Override
        public Month createFromParcel(Parcel source) {
            return new Month(source);
        }

        @Override
        public Month[] newArray(int size) {
            return new Month[size];
        }
    };
}
