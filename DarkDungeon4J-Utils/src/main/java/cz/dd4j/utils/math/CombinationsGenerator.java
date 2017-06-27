package cz.dd4j.utils.math;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Returns combinations (without repeating) of N elements out of some {@link List} of items.
 * @author Jimmy
 *
 * @param <T> item type
 */
public class CombinationsGenerator<T> implements Iterable<List<T>> {

	protected static abstract class CombinationsIteratorBase<T> implements Iterator<List<T>> {

		protected int count;
		protected List<T> items;
		
		protected int[] indices;
		
		/**
		 * Determined in {@link #computeNextIndices(int[], int)}.
		 */
		protected boolean hasNext;
		
		public CombinationsIteratorBase(int count, List<T> items) {
			this.count = count;
			this.items = items;
			this.indices = null;
			this.hasNext = true;
		}

		@Override
		public boolean hasNext() {
			if (indices == null) {
				if (count > items.size()) {
					return false;
				}
				if (count == 0) {
					indices = new int[0];
					return true;
				}
				init();
				return true;
			}
			return hasNext;
		}

		protected abstract void init();

		@Override
		public List<T> next() {
			if (indices == null) hasNext();
			if (!hasNext) return null;
			
			// COMPUTE RESULT
			List<T> result = new ArrayList<T>(count);
			
			if (count == 0) {
				hasNext = false;
				return result;
			}
			
			for (int i = 0; i < indices.length; ++i) {
				int index = indices[i];
				result.add(items.get(index));
			}
			
			// DETERMINE NEXT SET OF INDICES
			computeNextIndices(indices, count-1);
			
			return result;
		}

		/**
		 * Must correctly set {@link #hasNext} as the result.
		 * @param indices
		 * @param index
		 */
		protected abstract void computeNextIndices(int[] indices, int index);
		
	}
	
	private static class UniqueCombinationsIterator<T> extends CombinationsIteratorBase<T> {

		public UniqueCombinationsIterator(int count, List<T> items) {
			super(count, items);
		}

		@Override
		protected void init() {
			indices = new int[count];
			for (int i = 0; i < indices.length; ++i) {
				indices[i] = i; 
			}
			
		}

		@Override
		protected void computeNextIndices(int[] indices, int index) {
			if (index < 0) return;
			
			indices[index] += 1;
			if (indices[index] < items.size()) return;
			
			if (index == 0) {
				hasNext = false;
				return;
			}
			
			computeNextIndices(indices, index-1);
			if (!hasNext) {
				// NO MORE COMBINATIONS...
				return;
			}
			
			indices[index] = indices[index-1]+1;
			if (indices[index] < items.size()) return;
			
			hasNext = false;
		}
		
	}
	
	private static class NonUniqueCombinationsIterator<T> extends CombinationsIteratorBase<T> {

		public NonUniqueCombinationsIterator(int count, List<T> items) {
			super(count, items);
		}

		@Override
		protected void init() {
			indices = new int[count];
			for (int i = 0; i < indices.length; ++i) {
				indices[i] = 0; 
			}
			
		}

		@Override
		protected void computeNextIndices(int[] indices, int index) {
			if (index < 0) return;
			
			indices[index] += 1;
			if (indices[index] < items.size()) return;
			
			if (index == 0) {
				hasNext = false;
				return;
			}
			
			computeNextIndices(indices, index-1);
			if (!hasNext) {
				// NO MORE COMBINATIONS...
				return;
			}
			
			indices[index] = indices[index-1];
			if (indices[index] < items.size()) return;
			
			hasNext = false;
		}
		
	}
	
	private int count;
	private List<T> items;
	private boolean unique;
	
	public CombinationsGenerator(int count, List<T> items, boolean unique) {
		this.count = count;
		this.items = items;
		this.unique = unique;
	}
	
	@Override
	public Iterator<List<T>> iterator() {
		if (unique) { 
			return new UniqueCombinationsIterator<T>(count, items);
		} else {
			return new NonUniqueCombinationsIterator<T>(count, items);
		}		
	}
	
	public long totalCount() {
		if (unique) {
			long a = 1;
			long b = 1;
			for (int i = 0; i < count; ++i) {
				a *= (items.size() - i);
				b *= (count - i);
			}
			return a / b;
		} else {
			long a = 1;
			long b = 1;
			for (int i = 0; i < count; ++i) {
				a *= (items.size() + count - 1 - i);
				b *= (count - i);
			}
			return a / b;
		}
	}

}
