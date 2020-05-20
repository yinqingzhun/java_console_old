package com.yqz.console.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@code Builder} pattern implementation for the {@link Map}.
 *
 * @param <K> The Map key type.
 * @param <V> The Map value type.
 *
 * @author Artem Bilan
 *
 * @since 5.0
 */
public class MapBuilder< K, V> {

	private final Map<K, V> map = new HashMap<>();

	public MapBuilder put(K key, V value) {
		this.map.put(key, value);
		return _this();
	}

	public Map<K, V> get() {
		return this.map;
	}

	@SuppressWarnings("unchecked")
	protected final MapBuilder _this() { // NOSONAR name
		return   this;
	}

}