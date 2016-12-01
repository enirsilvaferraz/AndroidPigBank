package com.system.androidpigbank.models.firebase.dtos;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.table.DatabaseTable;
import com.system.androidpigbank.controllers.vos.CategoryVO;
import com.system.androidpigbank.models.firebase.serializers.GsonUtil;
import com.system.architecture.models.VOAbs;
import com.system.architecture.models.DTOAbs;

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
    public VOAbs toEntity() {
        return GsonUtil.getInstance().fromCategory().toEntity(this, CategoryVO.class);
    }
}
