package com.system.androidpigbank.models.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.system.androidpigbank.helpers.constant.Colors;

import java.util.List;

/**
 * Created by eferraz on 05/12/15.
 */
@DatabaseTable(tableName = "category")
public class Category extends EntityAbs implements Parcelable {

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

    @DatabaseField(allowGeneratedIdInsert = true, generatedId = true)
    private Long id;

    @DatabaseField
    private String name;

    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    private Colors color;

    private Float amount;
    private boolean expanded;
    private List<Transaction> transactionList;
    private List<Transaction> transactionSecundaryList;

    public Category() {
    }

    public Category(String name, Float amount) {
        this.name = name;
        this.amount = amount;
    }

    public Category(String name) {
        this.name = name;
    }

    protected Category(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        int tmpColor = in.readInt();
        this.color = tmpColor == -1 ? null : Colors.values()[tmpColor];
        this.amount = (Float) in.readValue(Float.class.getClassLoader());
        this.expanded = in.readByte() != 0;
        this.transactionList = in.createTypedArrayList(Transaction.CREATOR);
        this.transactionSecundaryList = in.createTypedArrayList(Transaction.CREATOR);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;

    }

    public Long getId() {
        return id;
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

    public List<Transaction> getTransactionSecundaryList() {
        return transactionSecundaryList;
    }

    public void setTransactionSecundaryList(List<Transaction> transactionSecundaryList) {
        this.transactionSecundaryList = transactionSecundaryList;
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
        dest.writeInt(this.color == null ? -1 : this.color.ordinal());
        dest.writeValue(this.amount);
        dest.writeByte(this.expanded ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.transactionList);
        dest.writeTypedList(this.transactionSecundaryList);
    }
}
