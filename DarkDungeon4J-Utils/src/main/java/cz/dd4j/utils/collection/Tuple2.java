package cz.dd4j.utils.collection;

import cz.dd4j.utils.Safe;

public class Tuple2<A, B> {

	int hashCode;
	
	public A a;
	public B b;

	public Tuple2(A a, B b) {
		this.a = a;
		this.b = b;
		this.hashCode = (a == null ? 7 : a.toString().hashCode()) * 21 + (b == null ? 7 : b.toString().hashCode());
	}

	@Override
	public int hashCode() {
		return hashCode;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof Tuple2)) return false;
		Tuple2 other = (Tuple2)obj;
		return Safe.equals(a, other.a) && Safe.equals(b,  other.b);
	}
}
