package com.system.androidpigbank.controllers.vos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.system.androidpigbank.controllers.helpers.Quinzena;
import com.system.androidpigbank.models.firebase.dtos.EstimateDTO;
import com.system.androidpigbank.models.firebase.serializers.GsonUtil;
import com.system.architecture.activities.CardAdapterAbs;
import com.system.architecture.models.DTOAbs;
import com.system.architecture.models.VOAbs;

/**
 * Created by Enir on 27/11/2016.
 */

public class EstimateVO extends VOAbs implements Parcelable, CardAdapterAbs.CardModel {

    public static final Creator<EstimateVO> CREATOR = new Creator<EstimateVO>() {
        @Override
        public EstimateVO createFromParcel(Parcel source) {
            return new EstimateVO(source);
        }

        @Override
        public EstimateVO[] newArray(int size) {
            return new EstimateVO[size];
        }
    };
    @Expose
    private String key;
    @Expose
    private CategoryVO category;
    @Expose
    private CategoryVO categorySecondary;
    @Expose
    private Integer day;
    @Expose
    private Double plannedValue;
    @Expose
    private Quinzena quinzena;

    private Double savedValue;
    private Double spentValue;
    private Double percentualVelue;

    public EstimateVO() {
    }

    protected EstimateVO(Parcel in) {
        this.key = in.readString();
        this.category = in.readParcelable(CategoryVO.class.getClassLoader());
        this.categorySecondary = in.readParcelable(CategoryVO.class.getClassLoader());
        this.day = (Integer) in.readValue(Integer.class.getClassLoader());
        this.plannedValue = (Double) in.readValue(Double.class.getClassLoader());
        int tmpQuinzena = in.readInt();
        this.quinzena = tmpQuinzena == -1 ? null : Quinzena.values()[tmpQuinzena];
        this.savedValue = (Double) in.readValue(Double.class.getClassLoader());
        this.spentValue = (Double) in.readValue(Double.class.getClassLoader());
        this.percentualVelue = (Double) in.readValue(Double.class.getClassLoader());
    }

    public CategoryVO getCategory() {
        return category;
    }

    public void setCategory(CategoryVO category) {
        this.category = category;
    }

    @Override
    public DTOAbs toDTO() {
        return GsonUtil.getInstance().fromEstimate().toDTO(this, EstimateDTO.class);
    }

    public Quinzena getQuinzena() {
        return quinzena;
    }

    public void setQuinzena(Quinzena quinzena) {
        this.quinzena = quinzena;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    public CategoryVO getCategorySecondary() {
        return categorySecondary;
    }

    public void setCategorySecondary(CategoryVO categorySecondary) {
        this.categorySecondary = categorySecondary;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Double getPlannedValue() {
        return plannedValue;
    }

    public void setPlannedValue(Double plannedValue) {
        this.plannedValue = plannedValue;
    }

    public Double getSavedValue() {
        return savedValue;
    }

    public void setSavedValue(Double savedValue) {
        this.savedValue = savedValue;
    }

    public Double getSpentValue() {
        return spentValue;
    }

    public void setSpentValue(Double spentValue) {
        this.spentValue = spentValue;
    }

    public Double getPercentualVelue() {
        return percentualVelue;
    }

    public void setPercentualVelue(Double percentualVelue) {
        this.percentualVelue = percentualVelue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeParcelable(this.category, flags);
        dest.writeParcelable(this.categorySecondary, flags);
        dest.writeValue(this.day);
        dest.writeValue(this.plannedValue);
        dest.writeInt(this.quinzena == null ? -1 : this.quinzena.ordinal());
        dest.writeValue(this.savedValue);
        dest.writeValue(this.spentValue);
        dest.writeValue(this.percentualVelue);
    }
}