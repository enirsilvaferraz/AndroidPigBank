package com.system.architecture.managers;

import com.system.androidpigbank.models.firebase.dtos.DTOAbs;

/**
 * Created by eferraz on 05/12/15.
 */
public abstract class EntityAbs {

    public abstract Long getId();

    public abstract DTOAbs toDTO();
}
