package com.system.androidpigbank.controllers.vos;

import android.os.Parcel;
import android.os.Parcelable;

import com.system.architecture.activities.CardAdapterAbs;

/**
 * Created by eferraz on 12/09/16.
 */

public class EndWhiteSpaceVO implements CardAdapterAbs.CardModel, Cloneable, Parcelable {

    public static final Creator<EndWhiteSpaceVO> CREATOR = new Creator<EndWhiteSpaceVO>() {
        @Override
        public EndWhiteSpaceVO createFromParcel(Parcel source) {
            return new EndWhiteSpaceVO(source);
        }

        @Override
        public EndWhiteSpaceVO[] newArray(int size) {
            return new EndWhiteSpaceVO[size];
        }
    };

    public EndWhiteSpaceVO() {
    }

    protected EndWhiteSpaceVO(Parcel in) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
