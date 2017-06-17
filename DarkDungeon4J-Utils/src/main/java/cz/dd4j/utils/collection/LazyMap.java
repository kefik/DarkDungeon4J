package cz.dd4j.utils.collection;

import java.util.HashMap;

public abstract class LazyMap<K, V> extends HashMap<K, V> {

	/**
	 * Auto-generated.
	 */
	private static final long serialVersionUID = 215138830639962554L;

	protected abstract V create(Object key);
	
	@Override
	public V get(Object key) {
		V value = super.get(key);
		if (value != null) return value;
		return put((K)key, create(key));				
	}
	
}
