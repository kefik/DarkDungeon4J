package cz.dd4j.utils.math;

import java.util.Iterator;
import java.util.List;

import cz.dd4j.utils.math.CombinationsGenerator.CombinationsIteratorBase;

/**
 * Returns combinations (without repeating) of N elements out of some {@link List} of items.
 * @author Jimmy
 *
 * @param <T> item type
 */
public class VariationsGenerator<T> implements Iterable<List<T>> {
	
	private static class UniqueVariationsIterator<T> extends CombinationsIteratorBase<T> {

		public UniqueVariationsIterator(int count, List<T> items) {
			super(count, items);
		}

		@Override
		protected void init() {
			indices = new int[count];
			for (int i = 0; i < indices.length; ++i) {
				indices[i] = -1;
				computeNextIndices(indices, i);
			}
			
		}

		@Override
		protected void computeNextIndices(int[] indices, int index) {
			if (index < 0) {
				hasNext = false;
				return;
			}
			
			do {
				indices[index] += 1;
			} while (!isIndexUniqueToPrevious(indices, index));
			
			if (indices[index] < items.size()) {
				// WE'RE DONE
				return;
			}
			
			// WE NEED TO CHANGE PREVIOUS INDEX AND REGENERATE THIS ONE
			computeNextIndices(indices, index-1);
				
			if (!hasNext) {
				// NO MORE ALTERNATIVES
				return;
			}
			
			// PREVIOUS INDEX GENERATED AND UNIQUE, TRY TO GENERATE THIS ONE
			indices[index] = -1;
			do {
				indices[index] += 1;
			} while (!isIndexUniqueToPrevious(indices, index));
			
			if (indices[index] < items.size()) {
				// WE'RE DONE
				return;
			}
			
			// WE HAVE FAILED TO FIND NEXT UNIQUE INDEX
			hasNext = false;
		}
		
		private boolean isIndexUniqueToPrevious(int[] indices, int index) {
			for (int i = index-1; i >= 0; --i) {
				if (indices[i] == indices[index]) return false;
			}
			return true;
		}
		
	}
	
	private static class NonUniqueVariationsIterator<T> extends CombinationsIteratorBase<T> {

		public NonUniqueVariationsIterator(int count, List<T> items) {
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
				// NO MORE VARIANTS...
				return;
			}
			
			indices[index] = 0;			
		}
		
	}

	private int count;
	private List<T> items;
	private boolean unique;
	
	public VariationsGenerator(int count, List<T> items, boolean unique) {
		this.count = count;
		this.items = items;
		this.unique = unique;
	}
	
	@Override
	public Iterator<List<T>> iterator() {
		if (unique) {
			return new UniqueVariationsIterator<T>(count, items);
		} else {
			return new NonUniqueVariationsIterator<T>(count, items);
		}		
	}
	
	public long totalCount() {
		if (unique) {
			long result = 1;
			for (int i = 0; i < count; ++i) {
				result *= (items.size() - i);
			}
			return result;
		} else {
			return (long) Math.pow(items.size(), count);
		}
	}

}
