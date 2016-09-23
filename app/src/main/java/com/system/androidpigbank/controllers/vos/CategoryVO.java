package com.system.androidpigbank.controllers.vos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.system.androidpigbank.controllers.helpers.Colors;
import com.system.androidpigbank.models.firebase.dtos.CategoryDTO;
import com.system.androidpigbank.models.firebase.dtos.DTOAbs;
import com.system.architecture.adapters.CardAdapter;
import com.system.architecture.managers.EntityAbs;
import com.system.architecture.utils.JavaUtils;

/**
 * Created by eferraz on 05/12/15.
 * Value Object de Categoria
 */
@DatabaseTable(tableName = "category")
public class CategoryVO extends EntityAbs implements VOIf, Parcelable, CardAdapter.CardModel, Cloneable {

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
    private CategoryVO old;

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
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.primary = in.readByte() != 0;
        int tmpColor = in.readInt();
        this.color = tmpColor == -1 ? null : Colors.values()[tmpColor];
        this.amount = (Double) in.readValue(Double.class.getClassLoader());
        this.old = in.readParcelable(CategoryVO.class.getClassLoader());
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

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public DTOAbs toDTO() {
        return JavaUtils.GsonUtil.getInstance().fromCategory().toDTO(this, CategoryDTO.class);
    }

    public Colors getColor() {
        return color;
    }

    public void setColor(Colors color) {
        this.color = color;
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
    public CardAdapter.CardViewType getViewType() {
        return CardAdapter.CardViewType.CARD_CATEGOTY;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public CategoryVO getOld() {
        return old;
    }

    public void setOld(CategoryVO old) {
        this.old = old;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeByte(this.primary ? (byte) 1 : (byte) 0);
        dest.writeInt(this.color == null ? -1 : this.color.ordinal());
        dest.writeValue(this.amount);
        dest.writeParcelable(this.old, flags);
    }
}
