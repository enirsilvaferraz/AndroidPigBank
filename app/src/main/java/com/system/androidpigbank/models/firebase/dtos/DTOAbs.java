package com.system.androidpigbank.models.firebase.dtos;

import com.system.androidpigbank.models.sqlite.entities.EntityAbs;

/**
 * Created by Enir on 07/09/2016.
 */

public abstract class DTOAbs {
    public abstract <T extends EntityAbs> T toEntity();
}
