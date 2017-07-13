package cz.dd4j.utils.collection;

import java.util.HashMap;

public class IntMap<K> extends HashMap<K, Integer> {

	@Override
	public Integer get(Object key) {
		if (key == null) return 0;
		if (!containsKey(key)) return 0;
		return super.get(key);
	}
	
	public int inc(K key, int delta) {
		if (key == null) return 0;
		if (!containsKey(key)) {
			put(key, delta);
			return 0;
		}
		return put(key, get(key)+delta);
	}
	
	public int inc(K key) {
		return inc(key, 1);
	}
	
	public int dec(K key, int delta) {
		if (key == null) return 0;
		if (!containsKey(key)) {
			put(key, -delta);
			return 0;
		}
		return put(key, get(key)-delta);
	}
	
	public int dec(K key) {
		return dec(key, 1);
	}
	
	public int sum() {
		int result = 0;
		for (Integer value : values()) {
			result += value;
		}
		return result;
	}
	
}
