package com.sopromadze.blogapi.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.exception.NotAcceptableException;
import com.sopromadze.blogapi.model.v2.MMenu;
import org.hibernate.Hibernate;
import org.springframework.util.ReflectionUtils;
//import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.Field;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectTransformUtil {


    public static <T> Map<String, Object> convertToMap( T mainObj) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(mainObj, new TypeReference<Map<String, Object>>() {
        });
    }



    public static <T> void partialMapper(Map<String, Object> updates, T existingObj) {
        if (updates == null || existingObj == null) {
            throw new IllegalArgumentException("Updates map and existing object must not be null.");
        }

        T unproxiedObj = (T) Hibernate.unproxy(existingObj);
        Class<?> clazz = unproxiedObj.getClass();

        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String fieldName = entry.getKey();
            Object newValue = entry.getValue();

            try {
                Field field = clazz.getDeclaredField(fieldName);

                if (field.getName().equals("id")) {
                    continue;
                }
                field.setAccessible(true);

                if (newValue != null && !field.getType().isAssignableFrom(newValue.getClass())) {
                    // Handle Integer to Long or Integer conversion
                    if (field.getType().equals(Long.class) && newValue.getClass().equals(Integer.class)) {
                        newValue = Long.valueOf((Integer) newValue); // Convert Integer to Long
                    } else if (field.getType().equals(Integer.class) && newValue.getClass().equals(Long.class)) {
                        newValue = ((Long) newValue).intValue(); // Convert Long to Integer
                    } else {
                        throw new NotAcceptableException(
                                "Type mismatch for field '" + fieldName + "'. Expected: " + field.getType().getName() + ", But received: " + newValue.getClass()
                        );
                    }
                }

                field.set(unproxiedObj, newValue);

            } catch (NoSuchFieldException e) {
                throw new NotAcceptableException("Field '" + fieldName + "' does not exist ");
            } catch (IllegalAccessException e) {
                throw new NotAcceptableException("Failed to access field: " + fieldName);
            }
        }
    }

}
