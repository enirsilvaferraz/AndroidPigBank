package com.system.androidpigbank.architecture.helpers;

import java.lang.reflect.ParameterizedType;

/**
 * Created by eferraz on 23/04/16.
 */
public class JavaHelper {

    public static Class getTClass(Object object) {
        final ParameterizedType type = (ParameterizedType) object.getClass().getGenericSuperclass();
        return (Class) (type).getActualTypeArguments()[0];
    }

    public static boolean isEmpty(Object object) {

        if (object == null) {
            return true;
        } else if (object instanceof String) {
            return ((String) object).isEmpty();
        }

        return false;
    }
}
