package com.system.androidpigbank.controllers.vos;

import android.os.Parcel;
import android.os.Parcelable;

import com.system.architecture.activities.CardAdapterAbs;

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
    private MonthVO currentMonth;
    private List<CardAdapterAbs.CardModel> listCategorySummary;
    private List<CardAdapterAbs.CardModel> listTransaction;
    private List<CardAdapterAbs.CardModel> listMonth;
    private List<CardAdapterAbs.CardModel> listEstimate;

    public HomeObjectVO() {
        listCategorySummary = new ArrayList<>();
        listTransaction = new ArrayList<>();
        listMonth = new ArrayList<>();
        currentMonth = new MonthVO();
        listEstimate = new ArrayList<>();
    }

    protected HomeObjectVO(Parcel in) {
        this.currentMonth = in.readParcelable(MonthVO.class.getClassLoader());
        this.listCategorySummary = new ArrayList<CardAdapterAbs.CardModel>();
        in.readList(this.listCategorySummary, CardAdapterAbs.CardModel.class.getClassLoader());
        this.listTransaction = new ArrayList<CardAdapterAbs.CardModel>();
        in.readList(this.listTransaction, CardAdapterAbs.CardModel.class.getClassLoader());
        this.listMonth = new ArrayList<CardAdapterAbs.CardModel>();
        in.readList(this.listMonth, CardAdapterAbs.CardModel.class.getClassLoader());
        this.listEstimate = new ArrayList<CardAdapterAbs.CardModel>();
        in.readList(this.listEstimate, CardAdapterAbs.CardModel.class.getClassLoader());
    }

    public List<CardAdapterAbs.CardModel> getListCategorySummary() {
        return listCategorySummary;
    }

    public void setListCategorySummary(List<CardAdapterAbs.CardModel> listCategorySummary) {
        this.listCategorySummary = listCategorySummary;
    }

    public List<CardAdapterAbs.CardModel> getListTransaction() {
        return listTransaction;
    }

    public void setListTransaction(List<CardAdapterAbs.CardModel> listTransaction) {
        this.listTransaction = listTransaction;
    }

    public List<CardAdapterAbs.CardModel> getListMonth() {
        return listMonth;
    }

    public void setListMonth(List<CardAdapterAbs.CardModel> listMonth) {
        this.listMonth = listMonth;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public MonthVO getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(MonthVO currentMonth) {
        this.currentMonth = currentMonth;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.currentMonth, flags);
        dest.writeList(this.listCategorySummary);
        dest.writeList(this.listTransaction);
        dest.writeList(this.listMonth);
        dest.writeList(this.listEstimate);
    }

    public List<CardAdapterAbs.CardModel> getListEstimate() {
        return listEstimate;
    }

    public void setListEstimate(List<CardAdapterAbs.CardModel> listEstimate) {
        this.listEstimate = listEstimate;
    }
}
