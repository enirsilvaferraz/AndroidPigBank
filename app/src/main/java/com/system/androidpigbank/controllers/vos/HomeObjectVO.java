package com.system.androidpigbank.controllers.vos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eferraz on 29/07/16.
 */

public class HomeObjectVO implements Parcelable {

    private int month;
    private int year;
    private List<CategoryVO> listCategorySummary;
    private List<TransactionVO> listTransaction;
    private List<MonthVO> listMonth;

    public HomeObjectVO() {
        listCategorySummary = new ArrayList<>();
        listTransaction = new ArrayList<>();
        listMonth = new ArrayList<>();
    }

    public List<CategoryVO> getListCategorySummary() {
        return listCategorySummary;
    }

    public void setListCategorySummary(List<CategoryVO> listCategorySummary) {
        this.listCategorySummary = listCategorySummary;
    }

    public List<TransactionVO> getListTransaction() {
        return listTransaction;
    }

    public void setListTransaction(List<TransactionVO> listTransaction) {
        this.listTransaction = listTransaction;
    }

    public List<MonthVO> getListMonth() {
        return listMonth;
    }

    public void setListMonth(List<MonthVO> listMonth) {
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
        dest.writeTypedList(this.listCategorySummary);
        dest.writeTypedList(this.listTransaction);
        dest.writeTypedList(this.listMonth);
    }

    protected HomeObjectVO(Parcel in) {
        this.month = in.readInt();
        this.year = in.readInt();
        this.listCategorySummary = in.createTypedArrayList(CategoryVO.CREATOR);
        this.listTransaction = in.createTypedArrayList(TransactionVO.CREATOR);
        this.listMonth = in.createTypedArrayList(MonthVO.CREATOR);
    }

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
}
