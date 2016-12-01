package com.system.architecture.models;

import android.os.Parcelable;

/**
 * Created by eferraz on 05/12/15.
 */
public abstract class VOAbs implements Parcelable, Cloneable {

    private VOAbs old;

    public abstract DTOAbs toDTO();

    public abstract String getKey();

    public abstract void setKey(String key);

    public VOAbs getOld() {
        return old;
    }

    public void setOld(VOAbs old) {
        this.old = old;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
