package com.system.androidpigbank.controllers.vos;

import android.os.Parcel;
import android.os.Parcelable;

import com.system.architecture.activities.CardAdapterAbs;

/**
 * Created by Enir on 17/08/2016.
 */

public class TitleVO implements CardAdapterAbs.CardModel, Cloneable, Parcelable {

    public static final Creator<TitleVO> CREATOR = new Creator<TitleVO>() {
        @Override
        public TitleVO createFromParcel(Parcel source) {
            return new TitleVO(source);
        }

        @Override
        public TitleVO[] newArray(int size) {
            return new TitleVO[size];
        }
    };
    private String title;

    public TitleVO(String title) {
        this.title = title;
    }

    protected TitleVO(Parcel in) {
        this.title = in.readString();
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
    }
}
