package com.yqz.console.utils;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.Field;

@Slf4j
public class BeanHelper {

    public static <T> T copy(Object src, Class<T> desClazz) {
        Preconditions.checkNotNull(src); 
        try {
            T dest = desClazz.newInstance();
            PropertyUtils.copyProperties(dest, src);
            return dest;
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        return null;
    }

    public static void copy(Object src, Object dest) {
        Preconditions.checkNotNull(src);
        try {
            PropertyUtils.copyProperties(dest, src);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
    public static <T> T copyIgnoreMismatchingFiledType(Object src, Class<T> desClazz) {
        Preconditions.checkNotNull(src);


        try {
            Class srcClazz = src.getClass();
            T dest = desClazz.newInstance();
            Field[] fields = desClazz.getDeclaredFields();
            for (Field field : fields) {
                try {
                    Field srcField = srcClazz.getDeclaredField(field.getName());
                    srcField.setAccessible(true);
                    field.setAccessible(true);
                    field.set(dest, srcField.get(src));
                } catch (Exception e) {
                    log.trace(e.getMessage());
                }

            }
            return dest;
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        return null;
    }
}
