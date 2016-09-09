package com.system.androidpigbank.models.entities;

import com.system.androidpigbank.models.dtos.DTOAbs;

/**
 * Created by eferraz on 05/12/15.
 */
public abstract class EntityAbs {

    public abstract Long getId();

    public abstract DTOAbs toDTO();
}
