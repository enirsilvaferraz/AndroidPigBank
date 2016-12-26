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

import java.util.Date;

/**
 * Created by Enir on 27/11/2016.
 */

public class EstimateVO extends VOAbs implements Parcelable, CardAdapterAbs.CardModel {

    @Expose
    private String key;
    @Expose
    private CategoryVO category;
    @Expose
    private CategoryVO categorySecondary;
    @Expose
    private Quinzena quinzena;
    @Expose
    private Double plannedValue;
    @Expose
    private Integer month;
    @Expose
    private Integer year;

    @Expose
    private Date date;

    private Double savedValue;
    private Double spentValue;
    private Double percentualVelue;
    private Double acumulateValue;
    private boolean registred;

    public EstimateVO() {
        plannedValue = 0D;
        savedValue = 0D;
        spentValue = 0D;
        percentualVelue = 0D;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPercentualVelue(Double percentualVelue) {
        this.percentualVelue = percentualVelue;
    }

    /*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EstimateVO that = (EstimateVO) o;

        if (category != null ? !category.equals(that.category) : that.category != null)
            return false;
        if (categorySecondary != null ? !categorySecondary.equals(that.categorySecondary) : that.categorySecondary != null)
            return false;
        if (day != null ? !day.equals(that.day) : that.day != null) return false;
        return quinzena == that.quinzena;

    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EstimateVO that = (EstimateVO) o;

        if (!category.equals(that.category)) return false;
        if (categorySecondary != null ? !categorySecondary.equals(that.categorySecondary) : that.categorySecondary != null)
            return false;
        return quinzena == that.quinzena;

    }

    public Double getAcumulateValue() {
        return acumulateValue;
    }

    public void setAcumulateValue(Double acumulateValue) {
        this.acumulateValue = acumulateValue;
    }

    @Override
    public int hashCode() {
        int result = category != null ? category.hashCode() : 0;
        result = 31 * result + (quinzena != null ? quinzena.hashCode() : 0);
        return result;
    }

    public boolean isRegistred() {
        return registred;
    }

    public void setRegistred(boolean registred) {
        this.registred = registred;
    }

    @Override
    public String toString() {
        return "EstimateVO{" +
                "key='" + key + '\'' +
                ", category=" + category +
                ", categorySecondary=" + categorySecondary +
                ", quinzena=" + quinzena +
                ", month=" + month +
                ", year=" + year +
                '}';
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
        dest.writeInt(this.quinzena == null ? -1 : this.quinzena.ordinal());
        dest.writeValue(this.plannedValue);
        dest.writeValue(this.month);
        dest.writeValue(this.year);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeValue(this.savedValue);
        dest.writeValue(this.spentValue);
        dest.writeValue(this.percentualVelue);
        dest.writeValue(this.acumulateValue);
        dest.writeByte(this.registred ? (byte) 1 : (byte) 0);
    }

    protected EstimateVO(Parcel in) {
        this.key = in.readString();
        this.category = in.readParcelable(CategoryVO.class.getClassLoader());
        this.categorySecondary = in.readParcelable(CategoryVO.class.getClassLoader());
        int tmpQuinzena = in.readInt();
        this.quinzena = tmpQuinzena == -1 ? null : Quinzena.values()[tmpQuinzena];
        this.plannedValue = (Double) in.readValue(Double.class.getClassLoader());
        this.month = (Integer) in.readValue(Integer.class.getClassLoader());
        this.year = (Integer) in.readValue(Integer.class.getClassLoader());
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.savedValue = (Double) in.readValue(Double.class.getClassLoader());
        this.spentValue = (Double) in.readValue(Double.class.getClassLoader());
        this.percentualVelue = (Double) in.readValue(Double.class.getClassLoader());
        this.acumulateValue = (Double) in.readValue(Double.class.getClassLoader());
        this.registred = in.readByte() != 0;
    }

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
}