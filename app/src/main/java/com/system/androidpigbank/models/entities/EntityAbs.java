package com.system.androidpigbank.models.entities;

/**
 * Created by eferraz on 05/12/15.
 */
public abstract class EntityAbs {

    public abstract Long getId();

    public abstract boolean isAlreadySync();

    public abstract void setAlreadySync(boolean alreadySync);

    public abstract boolean isActive();

    public abstract void setActive(boolean active);
}
