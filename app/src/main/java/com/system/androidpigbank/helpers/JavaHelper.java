package com.system.androidpigbank.helpers;

import java.lang.reflect.ParameterizedType;

/**
 * Created by eferraz on 23/04/16.
 */
public class JavaHelper {

    public static Class getTClass(Object object) {
        final ParameterizedType type = (ParameterizedType) object.getClass().getGenericSuperclass();
        return (Class) (type).getActualTypeArguments()[0];
    }
}