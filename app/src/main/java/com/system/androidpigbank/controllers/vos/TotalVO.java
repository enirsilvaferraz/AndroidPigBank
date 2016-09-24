package com.system.androidpigbank.controllers.vos;

import android.os.Parcel;

import com.system.architecture.adapters.CardAdapter;

/**
 * Created by eferraz on 03/01/16.
 */
public class TotalVO implements VOIf, CardAdapter.CardModel {

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
    private Double valueStart;
    private Double valueEnd;

    public TotalVO(Double valueStart, Double valueEnd) {
        this.valueStart = valueStart;
        this.valueEnd = valueEnd;
    }

    protected TotalVO(Parcel in) {
        this.valueStart = (Double) in.readValue(Double.class.getClassLoader());
        this.valueEnd = (Double) in.readValue(Double.class.getClassLoader());
    }

    public Double getValueStart() {
        return valueStart;
    }

    public Double getValueEnd() {
        return valueEnd;
    }

    @Override
    public CardAdapter.CardViewType getViewType() {
        return CardAdapter.CardViewType.CARD_FOOTER;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.valueStart);
        dest.writeValue(this.valueEnd);
    }
}
