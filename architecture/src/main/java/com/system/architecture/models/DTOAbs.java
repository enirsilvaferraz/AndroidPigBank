package com.system.architecture.models;

/**
 * Created by Enir on 07/09/2016.
 */

public abstract class DTOAbs {
    public abstract <T extends VOAbs> T toEntity();
}
