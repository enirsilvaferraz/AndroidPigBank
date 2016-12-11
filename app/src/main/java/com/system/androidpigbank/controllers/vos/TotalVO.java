package com.system.androidpigbank.controllers.vos;

import android.os.Parcel;
import android.os.Parcelable;

import com.system.architecture.activities.CardAdapterAbs;
import com.system.architecture.utils.JavaUtils;

/**
 * Created by eferraz on 03/01/16.
 */
public class TotalVO implements CardAdapterAbs.CardModel, Cloneable, Parcelable {

    public static final Creator<TotalVO> CREATOR = new Creator<TotalVO>() {
        @Override
        public TotalVO createFromParcel(Parcel source) {
            return new TotalVO(source);
        }

        @Override
        public TotalVO[] newArray(int size) {
            return new TotalVO[size];
        }
    };
    private String valueStart;
    private String valueEnd;

    public TotalVO(Double valueStart, Double valueEnd) {
        this.valueStart = valueStart != null ? JavaUtils.NumberUtil.currencyFormat(valueStart) : null;
        this.valueEnd = valueEnd != null ? JavaUtils.NumberUtil.currencyFormat(valueEnd) : null;
    }

    public TotalVO(String valueStart, String valueEnd) {
        this.valueStart = valueStart;
        this.valueEnd = valueEnd;
    }

    protected TotalVO(Parcel in) {
        this.valueStart = in.readString();
        this.valueEnd = in.readString();
    }

    public String getValueStart() {
        return valueStart;
    }

    public void setValueStart(String valueStart) {
        this.valueStart = valueStart;
    }

    public String getValueEnd() {
        return valueEnd;
    }

    public void setValueEnd(String valueEnd) {
        this.valueEnd = valueEnd;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.valueStart);
        dest.writeString(this.valueEnd);
    }
}
