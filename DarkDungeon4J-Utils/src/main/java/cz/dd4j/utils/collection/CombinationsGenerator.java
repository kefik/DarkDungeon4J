package cz.dd4j.utils.collection;

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

	private static abstract class CombinationsIteratorBase<T> implements Iterator<List<T>> {

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
	private boolean variations;
	
	public CombinationsGenerator(int count, List<T> items, boolean unique, boolean variations) {
		this.count = count;
		this.items = items;
		this.unique = unique;
		this.variations = variations;
	}
	
	@Override
	public Iterator<List<T>> iterator() {
		if (variations) {
			if (unique) {
				return new UniqueVariationsIterator<T>(count, items);
			} else {
				return new NonUniqueVariationsIterator<T>(count, items);
			}
		} else {
			if (unique) { 
				return new UniqueCombinationsIterator<T>(count, items);
			} else {
				return new NonUniqueCombinationsIterator<T>(count, items);
			}
		}
	}

}
