package com.system.architecture.utils;

import com.system.architecture.managers.EntityAbs;

/**
 * Created by Enir on 07/09/2016.
 */

public abstract class DTOAbs {
    public abstract <T extends EntityAbs> T toEntity();
}