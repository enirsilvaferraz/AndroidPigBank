package com.system.architecture.managers;

import com.system.androidpigbank.models.firebase.dtos.DTOAbs;

/**
 * Created by eferraz on 05/12/15.
 */
public abstract class EntityAbs {
    
    private String key;

    public abstract Long getId();

    public abstract DTOAbs toDTO();

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
