/*
 * Created on 2006/08/12
 *
 */
package org.middleheaven.sequence;

import java.util.Random;

public abstract class RandomNumberSequence <T> implements RandomSequence<T>{


	public static RandomNumberSequence<Long> newLongSequence(){
		return new RandomLongSequence( new Random()); 
	}

	public static RandomNumberSequence<Double> newDoubleSequence(){
		return new RandomDoubleSequence( new Random()); 
	}
	
	public static RandomNumberSequence<Long> newLongSequence(Random random){
		return new RandomLongSequence( random); 
	}

	public static RandomNumberSequence<Double> newDoubleSequence(Random random){
		return new RandomDoubleSequence( random); 
	}

	Random random;
	public RandomNumberSequence(Random random){
		this.random = random;
	}
	
	
	private static class RandomLongSequence extends RandomNumberSequence<Long>{

		public RandomLongSequence(Random random){
			super(random);
		}

		@Override
		public SequenceToken<Long> next() {

			return new DefaultToken<Long>(Long.valueOf(random.nextLong()));

		}

	}


	private static class RandomDoubleSequence extends RandomNumberSequence<Double>{

		public RandomDoubleSequence(Random random){
			super(random);
		}
		
		@Override
		public SequenceToken<Double> next() {
			return new DefaultToken<Double>(Double.valueOf(random.nextDouble()));
		}

	}


}
