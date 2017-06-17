package cz.dd4j.generator.adventure;

import java.util.Iterator;

public class Range implements Iterator<Integer> {
	
	public Range(int from, int to) {
		this.from = from;
		this.to = to;
	}
	
	public int from;
	
	public int to;
	
	private int current;
	
	public void reset() {
		current = from;
	}

	@Override
	public boolean hasNext() {
		return current < to;
	}

	@Override
	public Integer next() {
		++current;
		return current;
	}
	
}
