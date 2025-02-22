package com.banquito.marca.compartido.utilidades;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class UtilidadObjeto
{
    public static Map<String, Object> convertToMap(Object obj) throws IllegalAccessException {
        if (obj == null) {
            throw new IllegalArgumentException("El objeto no puede ser nulo");
        }

        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }

        return map;
    }
}
