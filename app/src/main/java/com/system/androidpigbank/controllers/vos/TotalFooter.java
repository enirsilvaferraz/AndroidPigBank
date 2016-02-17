package com.system.androidpigbank.controllers.vos;

import android.os.Parcel;
import android.os.Parcelable;

import com.system.androidpigbank.models.entities.EntityAbs;

/**
 * Created by eferraz on 03/01/16.
 */
public class TotalFooter extends EntityAbs implements Parcelable {

    public static final Creator<TotalFooter> CREATOR = new Creator<TotalFooter>() {
        public TotalFooter createFromParcel(Parcel source) {
            return new TotalFooter(source);
        }

        public TotalFooter[] newArray(int size) {
            return new TotalFooter[size];
        }
    };
    private Double total;

    public TotalFooter() {
    }

    protected TotalFooter(Parcel in) {
        this.total = (Double) in.readValue(Double.class.getClassLoader());
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(final Double total) {
        this.total = total;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.total);
    }
}
