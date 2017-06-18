package cz.dd4j.generator.adventure;

import java.util.Iterator;

public class Range implements Iterable<Integer> {
	
	private static class RangeIterator implements Iterator<Integer> {

		private final Range range;
		
		private int current;
		
		public RangeIterator(Range range) {
			this.range = range;
			current = range.from-1;
		}
		
		@Override
		public boolean hasNext() {
			return current < range.to;
		}

		@Override
		public Integer next() {
			++current;
			return current;
		}
		
	}
	
	public final int from;
	
	public final int to;
	
	
	public Range(int from, int to) {
		this.from = from;
		this.to = to;
	}
	
	@Override
	public RangeIterator iterator() {
		return new RangeIterator(this);
	}
	
}
