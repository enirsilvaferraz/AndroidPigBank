package com.system.androidpigbank.controllers.vos;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.table.DatabaseTable;
import com.system.androidpigbank.models.firebase.dtos.CategoryDTO;
import com.system.androidpigbank.models.firebase.serializers.GsonUtil;
import com.system.architecture.activities.CardAdapterAbs;
import com.system.architecture.models.DTOAbs;
import com.system.architecture.models.VOAbs;

/**
 * Created by eferraz on 05/12/15.
 * Value Object de Categoria
 */
@DatabaseTable(tableName = "category")
public class CategoryVO extends VOAbs implements CardAdapterAbs.CardModel {

    public static final Creator<CategoryVO> CREATOR = new Creator<CategoryVO>() {
        @Override
        public CategoryVO createFromParcel(Parcel source) {
            return new CategoryVO(source);
        }

        @Override
        public CategoryVO[] newArray(int size) {
            return new CategoryVO[size];
        }
    };

    @Expose
    private String key;
    @Expose
    private String name;
    @Expose
    private boolean primary;
    @Expose
    private Double amount;

    private MonthVO monthVO;

    public CategoryVO() {
        amount = 0D;
    }

    public CategoryVO(String name, Double amount) {
        this.name = name;
        this.amount = amount;
    }

    public CategoryVO(String name) {
        this.name = name;
    }

    protected CategoryVO(Parcel in) {
        this.key = in.readString();
        this.name = in.readString();
        this.primary = in.readByte() != 0;
        this.amount = (Double) in.readValue(Double.class.getClassLoader());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    @Override
    public DTOAbs toDTO() {
        return GsonUtil.getInstance().fromCategory().toDTO(this, CategoryDTO.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryVO that = (CategoryVO) o;

        return key != null ? key.equals(that.key) : that.key == null;

    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public MonthVO getMonthVO() {
        return monthVO;
    }

    public void setMonthVO(MonthVO monthVO) {
        this.monthVO = monthVO;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeByte(this.primary ? (byte) 1 : (byte) 0);
        dest.writeValue(this.amount);
    }

    @Override
    public String toString() {
        return "CategoryVO{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
