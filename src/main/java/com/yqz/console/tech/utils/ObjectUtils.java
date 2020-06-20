package com.yqz.console.tech.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class ObjectUtils {
	public static <T> T mapToObject(Map<?,?> map, Class<T> beanClass) {
		if (map == null)
			return null;

		T obj = null;
		try {
			obj = beanClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			org.apache.commons.beanutils.BeanUtils.populate(obj, map);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

	public static Map<?, ?> objectToMap(Object obj) {
		if (obj == null)
			return null;

		return new org.apache.commons.beanutils.BeanMap(obj);
	}

	/*
	 * public static <T> T mapToObject(Map map, Class<T> beanClass) { if (map ==
	 * null) return null;
	 * 
	 * T obj=null; try { obj = beanClass.newInstance(); } catch
	 * (InstantiationException | IllegalAccessException e) {
	 * e.printStackTrace(); }
	 * 
	 * Field[] fields = obj.getClass().getDeclaredFields(); for (Field field :
	 * fields) { int mod = field.getModifiers(); if(Modifier.isStatic(mod) ||
	 * Modifier.isFinal(mod)){ continue; }
	 * 
	 * field.setAccessible(true); try { field.set(obj,
	 * map.get(field.getName())); } catch (IllegalArgumentException |
	 * IllegalAccessException e) { e.printStackTrace(); } }
	 * 
	 * return obj; }
	 * 
	 * public static <T> Map objectToMap(T obj) { if(obj == null){ return null;
	 * }
	 * 
	 * Map<String, Object> map = new HashMap<String, Object>();
	 * 
	 * Field[] declaredFields = obj.getClass().getDeclaredFields(); for (Field
	 * field : declaredFields) { field.setAccessible(true); try {
	 * map.put(field.getName(), field.get(obj)); } catch
	 * (IllegalArgumentException | IllegalAccessException e) {
	 * e.printStackTrace(); } }
	 * 
	 * return map; }
	 */
}
