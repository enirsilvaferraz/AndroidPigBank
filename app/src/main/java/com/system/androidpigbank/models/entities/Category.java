package com.system.androidpigbank.models.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

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

    //@DatabaseField
    private Float amount;

    @DatabaseField
    private boolean alreadySync;

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
        this.amount = (Float) in.readValue(Float.class.getClassLoader());
        this.alreadySync = in.readByte() != 0;
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

    public boolean isAlreadySync() {
        return alreadySync;
    }

    public void setAlreadySync(boolean alreadySync) {
        this.alreadySync = alreadySync;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeValue(this.amount);
        dest.writeByte(alreadySync ? (byte) 1 : (byte) 0);
    }
}
