/**
 * 
 */
package org.middleheaven.collections.enumerable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


/**
 * 
 */
class RandomEnumerable<T> extends AbstractEnumerable<T> implements FastCountEnumerable{

	private int samplesCount;
	private Enumerable<T> original;
	private Random random;
	
	public RandomEnumerable(int samplesCount , Enumerable<T> original, Random random){
		this.samplesCount = samplesCount;
		this.original = original;
		this.random = random;
	}
	/**
	 * {@inheritDoc}
	 * 
	 * @see http://en.wikipedia.org/wiki/Reservoir_sampling
	 * @see http://gregable.com/2007/10/reservoir-sampling.html
	 */
	@Override
	public Iterator<T> iterator() {
		List<T> reservoir = new ArrayList<T>(samplesCount);
		
		int iterationCount =0;
		Iterator<T> iterator = original.iterator();
		while ( iterationCount < samplesCount && iterator.hasNext()){
			reservoir.add(iterator.next());
			iterationCount++;
		}
		
		while (iterator.hasNext()){
			T t = iterator.next();
			
		    int replaceIndex = (int) Math.floor((double) iterationCount * random.nextDouble());
		    // if the index exists in the reservoire
		    if (replaceIndex < samplesCount) {
		    	reservoir.set(replaceIndex, t);
		    }

			iterationCount++;
		}
		
		return reservoir.iterator();
	}
	
	@Override
	public int size(){
		return samplesCount;
	}
	

}
