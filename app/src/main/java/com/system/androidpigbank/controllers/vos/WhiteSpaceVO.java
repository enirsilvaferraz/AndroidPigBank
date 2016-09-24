package com.system.androidpigbank.controllers.vos;

import android.os.Parcel;

import com.system.architecture.adapters.CardAdapter;

/**
 * Created by eferraz on 12/09/16.
 */

public class WhiteSpaceVO implements VOIf, CardAdapter.CardModel {

    public static final Creator<WhiteSpaceVO> CREATOR = new Creator<WhiteSpaceVO>() {
        @Override
        public WhiteSpaceVO createFromParcel(Parcel source) {
            return new WhiteSpaceVO(source);
        }

        @Override
        public WhiteSpaceVO[] newArray(int size) {
            return new WhiteSpaceVO[size];
        }
    };

    public WhiteSpaceVO() {
    }

    protected WhiteSpaceVO(Parcel in) {
    }

    @Override
    public CardAdapter.CardViewType getViewType() {
        return CardAdapter.CardViewType.CARD_WHITESPACE;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
