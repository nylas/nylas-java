package com.nylas;

import java.lang.reflect.Field;

public class FieldReflectionUtils {
    public static void setField(String fieldName, Object fieldValue, Object o, boolean setOnParentClass) throws NoSuchFieldException, IllegalAccessException {

        Field codeField = null;

        if(setOnParentClass) {
            codeField = o.getClass().getSuperclass().getDeclaredField(fieldName);
        } else {
            codeField = o.getClass().getDeclaredField(fieldName);
        }

        codeField.setAccessible(true);
        codeField.set(o, fieldValue);
    }

    public static void setField(String fieldName, Object fieldValue, Object o, int parentDepth) throws NoSuchFieldException, IllegalAccessException {

        Field codeField = null;
        Class<?> topClass = null;

        for(int i = 0; i <= parentDepth; i++) {
            if (topClass == null) {
                topClass = o.getClass().getSuperclass();
            } else {
                topClass = topClass.getSuperclass();
            }
        }

        assert topClass != null;
        codeField = topClass.getDeclaredField(fieldName);

        codeField.setAccessible(true);
        codeField.set(o, fieldValue);
    }

    public static void setField(String fieldName, Object fieldValue, Object o) throws NoSuchFieldException, IllegalAccessException {
        Field codeField = o.getClass().getDeclaredField(fieldName);
        codeField.setAccessible(true);
        codeField.set(o, fieldValue);
    }

    public static Object getField(String fieldName, Object o) throws NoSuchFieldException, IllegalAccessException {
        Field declaredField = o.getClass().getDeclaredField(fieldName);
        declaredField.setAccessible(true);

        return declaredField.get(o);
    }
}
