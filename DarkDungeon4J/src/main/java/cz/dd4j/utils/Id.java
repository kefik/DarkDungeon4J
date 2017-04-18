package cz.dd4j.utils;

import java.util.HashMap;
import java.util.Map;

public class Id {
	
	private static int nextId = 1;
	
	private static Map<String, Id> str2Id = new HashMap<String, Id>();
	
	private static Map<Integer, Id> int2Id = new HashMap<Integer, Id>();
	
	private static Object mutex = new Object();
	
	public static Id get(String name) {
		Id result = str2Id.get(name);
		if (result == null) {
			synchronized(mutex) {
				result = str2Id.get(name);
				if (result != null) return result;
				result = new Id(nextId++, name);
				str2Id.put(result.name, result);
				int2Id.put(result.id, result);				
			}			
			return result;
		}
		return result;
	}
	
	public static Id get(int id) {
		Id result = int2Id.get(id);
		if (result == null) {
			synchronized(mutex) {
				if (id <= 0) throw new RuntimeException("Id can be only of positive value! Received: id = " + id);
				result = int2Id.get(id);
				if (result != null) return result;
				result = new Id(id, "ID[" + id + "]");
				str2Id.put(result.name, result);
				int2Id.put(result.id, result);
				nextId = id + 1;				
			}
			return result;			
		}
		return result;
	}
	
	public static Id next() {
		return get(nextId);
	}
	
	public static void reset() {
		synchronized(mutex) {
			nextId = 1;
			str2Id.clear();
			int2Id.clear();
		}
	}
	
	public final int id;
	
	public final String name;
	
	private Id(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof Id)) return false;
		Id other = (Id)obj;
		return id == other.id;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
