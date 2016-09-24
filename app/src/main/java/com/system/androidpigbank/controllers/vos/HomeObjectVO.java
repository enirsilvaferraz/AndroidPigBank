package com.system.androidpigbank.controllers.vos;

import android.os.Parcel;
import android.os.Parcelable;

import com.system.architecture.adapters.CardAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eferraz on 29/07/16.
 * Value Object da Home
 */

public class HomeObjectVO implements Parcelable {

    public static final Creator<HomeObjectVO> CREATOR = new Creator<HomeObjectVO>() {
        @Override
        public HomeObjectVO createFromParcel(Parcel source) {
            return new HomeObjectVO(source);
        }

        @Override
        public HomeObjectVO[] newArray(int size) {
            return new HomeObjectVO[size];
        }
    };
    private int month;
    private int year;
    private List<CardAdapter.CardModel> listCategorySummary;
    private List<CardAdapter.CardModel> listTransaction;
    private List<CardAdapter.CardModel> listMonth;

    public HomeObjectVO() {
        listCategorySummary = new ArrayList<>();
        listTransaction = new ArrayList<>();
        listMonth = new ArrayList<>();
    }

    protected HomeObjectVO(Parcel in) {
        this.month = in.readInt();
        this.year = in.readInt();
        this.listCategorySummary = new ArrayList<CardAdapter.CardModel>();
        in.readList(this.listCategorySummary, CardAdapter.CardModel.class.getClassLoader());
        this.listTransaction = new ArrayList<CardAdapter.CardModel>();
        in.readList(this.listTransaction, CardAdapter.CardModel.class.getClassLoader());
        this.listMonth = new ArrayList<CardAdapter.CardModel>();
        in.readList(this.listMonth, CardAdapter.CardModel.class.getClassLoader());
    }

    public List<CardAdapter.CardModel> getListCategorySummary() {
        return listCategorySummary;
    }

    public void setListCategorySummary(List<CardAdapter.CardModel> listCategorySummary) {
        this.listCategorySummary = listCategorySummary;
    }

    public List<CardAdapter.CardModel> getListTransaction() {
        return listTransaction;
    }

    public void setListTransaction(List<CardAdapter.CardModel> listTransaction) {
        this.listTransaction = listTransaction;
    }

    public List<CardAdapter.CardModel> getListMonth() {
        return listMonth;
    }

    public void setListMonth(List<CardAdapter.CardModel> listMonth) {
        this.listMonth = listMonth;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.month);
        dest.writeInt(this.year);
        dest.writeList(this.listCategorySummary);
        dest.writeList(this.listTransaction);
        dest.writeList(this.listMonth);
    }
}
