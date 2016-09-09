package com.system.androidpigbank.controllers.vos;

import android.os.Parcel;
import android.os.Parcelable;

import com.system.androidpigbank.models.dtos.DTOAbs;
import com.system.androidpigbank.models.entities.EntityAbs;

/**
 * Created by Enir on 17/08/2016.
 */

public class TitleVO extends EntityAbs implements Parcelable {

    private String title;

    public TitleVO(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public DTOAbs toDTO() {
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
    }

    public TitleVO() {
    }

    protected TitleVO(Parcel in) {
        this.title = in.readString();
    }

    public static final Parcelable.Creator<TitleVO> CREATOR = new Parcelable.Creator<TitleVO>() {
        @Override
        public TitleVO createFromParcel(Parcel source) {
            return new TitleVO(source);
        }

        @Override
        public TitleVO[] newArray(int size) {
            return new TitleVO[size];
        }
    };
}
