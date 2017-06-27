package cz.dd4j.utils;

public class Safe {

	public static boolean equals(Object a, Object b) {
		if (a == null) {
			return b == null;
		} else {
			if (b == null) return false;
			return a.equals(b);
		}
	}
	
}
