package com.system.androidpigbank.controllers.vos;

import android.os.Parcel;
import android.os.Parcelable;

import com.system.androidpigbank.models.entities.EntityAbs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by eferraz on 26/12/15.
 */
public class DateSection extends EntityAbs implements Parcelable {

    public static final Parcelable.Creator<DateSection> CREATOR = new Parcelable.Creator<DateSection>() {
        public DateSection createFromParcel(Parcel source) {
            return new DateSection(source);
        }

        public DateSection[] newArray(int size) {
            return new DateSection[size];
        }
    };

    private Date date;
    private String title;

    public DateSection() {
    }

    public DateSection(Date date) {
        this.date = date;
        this.title = DateFormat.getDateInstance(DateFormat.FULL).format(date);
        //this.title = new SimpleDateFormat("dd MMMM, yyyy").format(date);
    }

    protected DateSection(Parcel in) {
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.title = in.readString();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(date != null ? date.getTime() : -1);
        dest.writeString(this.title);
    }

    @Override
    public Long getId() {
        return null;
    }

}


