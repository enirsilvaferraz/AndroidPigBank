package com.system.architecture.managers;

import android.os.Parcelable;

import com.system.architecture.utils.DTOAbs;

/**
 * Created by eferraz on 05/12/15.
 */
public abstract class EntityAbs implements Parcelable, Cloneable {

    private EntityAbs old;

    public abstract DTOAbs toDTO();

    public abstract String getKey();

    public abstract void setKey(String key);

    public EntityAbs getOld() {
        return old;
    }

    public void setOld(EntityAbs old) {
        this.old = old;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
