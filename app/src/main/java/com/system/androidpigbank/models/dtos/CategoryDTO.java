package com.system.androidpigbank.models.dtos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.system.androidpigbank.controllers.helpers.constant.Colors;
import com.system.androidpigbank.models.entities.Category;
import com.system.androidpigbank.models.entities.EntityAbs;
import com.system.androidpigbank.models.entities.Transaction;
import com.system.architecture.utils.JavaUtils;

import java.util.List;

/**
 * Created by eferraz on 05/12/15.
 */
@DatabaseTable(tableName = "category")
public class CategoryDTO extends DTOAbs {

    @Expose
    private String key;

    @Expose
    private String name;

    @Expose
    private boolean primary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public EntityAbs toEntity() {
        return JavaUtils.GsonUtil.getInstance().fromCategory().toEntity(this, Category.class);
    }
}
