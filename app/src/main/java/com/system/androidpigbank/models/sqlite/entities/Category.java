package com.system.androidpigbank.models.sqlite.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.system.androidpigbank.controllers.helpers.constant.Colors;
import com.system.androidpigbank.models.firebase.dtos.CategoryDTO;
import com.system.androidpigbank.models.firebase.dtos.DTOAbs;
import com.system.architecture.utils.JavaUtils;

import java.util.List;

/**
 * Created by eferraz on 05/12/15.
 */
@DatabaseTable(tableName = "category")
public class Category extends EntityAbs implements Parcelable {

    @Expose
    @DatabaseField(allowGeneratedIdInsert = true, generatedId = true)
    private Long id;

    @Expose
    @DatabaseField
    private String name;

    @Expose
    @DatabaseField
    private boolean primary;

    @Expose
    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    private Colors color;

    private Double amount;
    private boolean expanded;
    private List<Transaction> transactionList;

    public Category() {
    }

    public Category(String name, Double amount) {
        this.name = name;
        this.amount = amount;
    }

    public Category(String name) {
        this.name = name;
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

    public Long getId() {
        return id;
    }

    @Override
    public DTOAbs toDTO() {
        return JavaUtils.GsonUtil.getInstance().fromCategory().toDTO(this, CategoryDTO.class);
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        return !(getName() != null ? !getName().equals(category.getName()) : category.getName() != null);
    }

    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
    }

    public Colors getColor() {
        return color;
    }

    public void setColor(Colors color) {
        this.color = color;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeByte(this.primary ? (byte) 1 : (byte) 0);
        dest.writeInt(this.color == null ? -1 : this.color.ordinal());
        dest.writeValue(this.amount);
        dest.writeByte(this.expanded ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.transactionList);
    }

    protected Category(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.primary = in.readByte() != 0;
        int tmpColor = in.readInt();
        this.color = tmpColor == -1 ? null : Colors.values()[tmpColor];
        this.amount = (Double) in.readValue(Double.class.getClassLoader());
        this.expanded = in.readByte() != 0;
        this.transactionList = in.createTypedArrayList(Transaction.CREATOR);
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}