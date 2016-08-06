package com.system.androidpigbank.models.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by eferraz on 05/12/15.
 */
@DatabaseTable(tableName = "fixed_transaction")
public class FixedTransaction extends EntityAbs implements Parcelable {

    public static final Creator<FixedTransaction> CREATOR = new Creator<FixedTransaction>() {
        @Override
        public FixedTransaction createFromParcel(Parcel source) {
            return new FixedTransaction(source);
        }

        @Override
        public FixedTransaction[] newArray(int size) {
            return new FixedTransaction[size];
        }
    };
    @DatabaseField(allowGeneratedIdInsert = true, generatedId = true)
    private Long id;
    @DatabaseField
    private Double value;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Category category;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Category categorySecondary;
    @DatabaseField
    private String content;


    public FixedTransaction() {
    }

    protected FixedTransaction(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.value = (Double) in.readValue(Double.class.getClassLoader());
        this.category = in.readParcelable(Category.class.getClassLoader());
        this.categorySecondary = in.readParcelable(Category.class.getClassLoader());
        this.content = in.readString();
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category getCategorySecondary() {
        return categorySecondary;
    }

    public void setCategorySecondary(Category categorySecondary) {
        this.categorySecondary = categorySecondary;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.value);
        dest.writeParcelable(this.category, flags);
        dest.writeParcelable(this.categorySecondary, flags);
        dest.writeString(this.content);
    }
}
